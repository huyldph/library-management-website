package com.example.librarymanagementwebsite.feature.bookcopy;

import com.example.librarymanagementwebsite.feature.bookcopy.dto.BookCopyResponse;
import com.example.librarymanagementwebsite.util.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/book-copies")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookCopyController {
    BookCopyService bookCopyService;

    @GetMapping
    public ApiResponse<?> getBookCopy(@RequestParam Integer bookId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        Page<BookCopyResponse> response = bookCopyService.getBookCopies(bookId, page, size);

        return ApiResponse.builder()
                .message("Get book copies for book with id: " + bookId)
                .result(response)
                .build();
    }

    @GetMapping("/{bookCopyId}")
    public ApiResponse<?> getBookCopyById(@PathVariable Integer bookCopyId) {
        BookCopyResponse response = bookCopyService.getBookCopyById(bookCopyId);
        return ApiResponse.builder()
                .message("Get book copy with id: " + bookCopyId)
                .result(response)
                .build();
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<?> createBookCopy(@RequestParam Integer bookId, @RequestParam Integer quantity) throws Exception {
        bookCopyService.createBookCopy(bookId, quantity);
        return ApiResponse.builder()
                .message("Created " + quantity + " copies for book with id: " + bookId)
                .build();
    }

    @PutMapping("/update-slot")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<?> updateSlot(@RequestParam Integer bookCopyId, @RequestParam Integer slotId) {
        bookCopyService.updateBookCopyStatus(bookCopyId, slotId);
        return ApiResponse.builder()
                .message("Updated slot for book copy with id: " + bookCopyId)
                .build();
    }
}
