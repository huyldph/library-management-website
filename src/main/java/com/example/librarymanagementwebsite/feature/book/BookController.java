package com.example.librarymanagementwebsite.feature.book;

import com.example.librarymanagementwebsite.util.ApiResponse;
import com.example.librarymanagementwebsite.feature.book.dto.BookRequest;
import com.example.librarymanagementwebsite.feature.book.dto.BookResponse;
import com.example.librarymanagementwebsite.feature.book.dto.BookListResult;
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
    ApiResponse<BookListResult> getAllBooks(@RequestParam(required = false) String query,
                                            @RequestParam(required = false) String category,
                                            @RequestParam int page,
                                            @RequestParam int size) {
        int zeroBasedPage = Math.max(0, page - 1); // FE sends 1-based, convert to 0-based
        var searchPage = bookService.searchBooks(query, category, zeroBasedPage, size);

        BookListResult payload = new BookListResult();
        payload.setItems(searchPage.getContent());
        payload.setTotal(searchPage.getTotalElements());

        return ApiResponse.<BookListResult>builder()
                .result(payload)
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
