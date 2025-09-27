package com.example.librarymanagementwebsite.feature.book;

import com.example.librarymanagementwebsite.feature.book.dto.BookRequest;
import com.example.librarymanagementwebsite.feature.book.dto.BookResponse;
import com.example.librarymanagementwebsite.feature.book.mapper.BookMapper;
import com.example.librarymanagementwebsite.feature.category.Category;
import com.example.librarymanagementwebsite.feature.category.CategoryRepository;
import com.example.librarymanagementwebsite.feature.publisher.Publisher;
import com.example.librarymanagementwebsite.feature.publisher.PublisherRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class BookService {
    BookRepository bookRepository;
    BookMapper bookMapper;
    CategoryRepository categoryRepository;
    PublisherRepository publisherRepository;

    public Page<BookResponse> getAllBooks(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        return bookRepository.findAll(pageable)
                .map(bookMapper::toResponse);
    }

    public BookResponse getBookById(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        return bookMapper.toResponse(book);
    }

    public Boolean deleteBook(Integer id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found");
        }
        bookRepository.deleteById(id);

        return true;
    }

    public BookResponse updateBook(Integer id, BookRequest request) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        bookMapper.updateBook(request, existingBook);
        existingBook.setCategory(category);
        existingBook.setPublisher(publisher);

        bookRepository.save(existingBook);

        return bookMapper.toResponse(existingBook);
    }

    public BookResponse createBook(BookRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(() -> new RuntimeException("Publisher not found"));

        Book book = bookMapper.toEntity(request);
        book.setIsbn(generateISBN13());
        book.setCategory(category);
        book.setPublisher(publisher);
        bookRepository.save(book);

        return bookMapper.toResponse(book);
    }

    private String generateISBN13() {
        Random random = new Random();

        // Bắt đầu bằng prefix hợp lệ (978 hoặc 979)
        int[] prefixOptions = {978, 979};
        int prefix = prefixOptions[random.nextInt(prefixOptions.length)];

        // Sinh 9 số tiếp theo (group + publisher + title)
        StringBuilder isbnBuilder = new StringBuilder(String.valueOf(prefix));
        for (int i = 0; i < 9; i++) {
            isbnBuilder.append(random.nextInt(10)); // 0-9
        }

        // Tính check digit
        String isbnWithoutCheckDigit = isbnBuilder.toString();
        int checkDigit = calculateCheckDigit(isbnWithoutCheckDigit);
        isbnBuilder.append(checkDigit);

        return isbnBuilder.toString();
    }

    private static int calculateCheckDigit(String isbn12) {
        if (isbn12.length() != 12) {
            throw new IllegalArgumentException("ISBN phải có 12 số để tính check digit");
        }

        int sum = 0;
        for (int i = 0; i < isbn12.length(); i++) {
            int digit = Character.getNumericValue(isbn12.charAt(i));
            sum += (i % 2 == 0) ? digit : digit * 3;
        }

        int mod = sum % 10;
        return (10 - mod) % 10;
    }
}