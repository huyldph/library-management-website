package com.example.librarymanagementwebsite.feature.bookcopy;

import com.example.librarymanagementwebsite.feature.book.Book;
import com.example.librarymanagementwebsite.feature.book.BookRepository;
import com.example.librarymanagementwebsite.feature.bookcopy.dto.BookCopyResponse;
import com.example.librarymanagementwebsite.feature.bookcopy.mapper.BookCopyMapper;
import com.example.librarymanagementwebsite.feature.shelves.ShelfSlot;
import com.example.librarymanagementwebsite.feature.shelves.ShelfSlotRepository;
import com.example.librarymanagementwebsite.util.BarcodeGenerator;
import com.example.librarymanagementwebsite.util.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class BookCopyService {
    BookCopyRepository bookCopyRepository;
    BookRepository bookRepository;
    CloudinaryService cloudinaryService;
    ShelfSlotRepository shelfSlotRepository;
    BookCopyMapper bookCopyMapper;

    public BookCopyResponse getBookCopyById(Integer bookCopyId) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new RuntimeException("Book copy not found with id: " + bookCopyId));
        return bookCopyMapper.toResponse(bookCopy);
    }

    public Page<BookCopyResponse> getBookCopies(Integer bookId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<BookCopy> entities = bookCopyRepository.findByBookId(bookId, pageable);

        return entities.map(bookCopyMapper::toResponse);
    }

    public void createBookCopy(Integer bookId, Integer quantity) throws Exception {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        for (int i = 0; i < quantity; i++) {
            // 1. Sinh chuỗi barcode
            String barcode = "BK-" + bookId + "-" + System.currentTimeMillis() + "-" + (int) (Math.random() * 1000);
            // Sinh barcode image
            byte[] imageBytes = BarcodeGenerator.generateToBytes(barcode);
            // Upload lên Cloudinary
            String imageUrl = cloudinaryService.upload(imageBytes, barcode);

            BookCopy bookCopy = BookCopy.builder()
                    .book(book)
                    .barcode(barcode)
                    .barcodeImageUrl(imageUrl)
                    .build();

            bookCopyRepository.save(bookCopy);
        }
    }

    public void updateBookCopyStatus(Integer bookCopyId, Integer slotId) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() -> new RuntimeException("Book copy not found with id: " + bookCopyId));

        ShelfSlot slot = shelfSlotRepository.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Shelf slot not found with id: " + slotId));
        bookCopy.setSlot(slot);

        bookCopyRepository.save(bookCopy);

        // Cập nhật trạng thái của slot
        slot.setIsOccupied(true);
        shelfSlotRepository.save(slot);
    }
}
