package com.example.librarymanagementwebsite.feature.book;

import com.example.librarymanagementwebsite.util.ApiResponse;
import com.example.librarymanagementwebsite.feature.book.dto.BookRequest;
import com.example.librarymanagementwebsite.feature.book.dto.BookResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class BookController {
    BookService bookService;

    @GetMapping
    ApiResponse<?> getAllBooks(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        var result = bookService.getAllBooks(page, size);
        return ApiResponse.builder()
                .result(result)
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<BookResponse> getBookById(@PathVariable Integer id) {
        var result = bookService.getBookById(id);
        return ApiResponse.<BookResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    ApiResponse<BookResponse> createBook(@RequestBody BookRequest request) {
        var result = bookService.createBook(request);
        return ApiResponse.<BookResponse>builder()
                .result(result)
                .build();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    ApiResponse<BookResponse> updateBook(@PathVariable Integer id, @RequestBody BookRequest request) {
        var result = bookService.updateBook(id, request);
        return ApiResponse.<BookResponse>builder()
                .result(result)
                .build();
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    ApiResponse<Boolean> deleteBook(@RequestParam Integer id) {
        var result = bookService.deleteBook(id);
        return ApiResponse.<Boolean>builder()
                .result(result)
                .build();
    }
}
