package com.example.librarymanagementwebsite.feature.category;

import com.example.librarymanagementwebsite.util.ApiResponse;
import com.example.librarymanagementwebsite.feature.category.dto.CategoryRequest;
import com.example.librarymanagementwebsite.feature.category.dto.CategoryResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryController {
    CategoryService categoryService;

    @GetMapping
    public ApiResponse<?> getCategories(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        Page<CategoryResponse> categories = categoryService.getCategories(page, size);

        return ApiResponse.builder()
                .result(categories)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getCategoryById(@PathVariable Integer id) {
        CategoryResponse category = categoryService.getCategoryById(id);

        return ApiResponse.<CategoryResponse>builder()
                .result(category)
                .build();
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        CategoryResponse category = categoryService.createCategory(request);

        return ApiResponse.<CategoryResponse>builder()
                .result(category)
                .build();
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<CategoryResponse> updateCategory(@RequestParam Integer id, @RequestBody CategoryRequest request) {
        CategoryResponse category = categoryService.updateCategory(id, request);

        return ApiResponse.<CategoryResponse>builder()
                .result(category)
                .build();
    }
}
