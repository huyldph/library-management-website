package com.example.librarymanagementwebsite.feature.category;

import com.example.librarymanagementwebsite.feature.category.dto.CategoryRequest;
import com.example.librarymanagementwebsite.feature.category.dto.CategoryResponse;
import com.example.librarymanagementwebsite.feature.category.mapper.CategoryMapper;
import com.example.librarymanagementwebsite.feature.shelves.Shelf;
import com.example.librarymanagementwebsite.feature.shelves.ShelfRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    CategoryRepository categoryRepository;
    ShelfRepository shelfRepository;
    CategoryMapper categoryMapper;

    public CategoryResponse getCategoryById(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        return categoryMapper.toResponse(category);
    }

    public Page<CategoryResponse> getCategories(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        return categoryPage.map(categoryMapper::toResponse);
    }

    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Set<Shelf> shelves = new HashSet<>();

        for (Integer id : categoryRequest.getShelfIds()) {
            Shelf shelf = shelfRepository.findById(id).
                    orElseThrow(() -> new RuntimeException("Shelf not found with id: " + id));

            shelves.add(shelf);
        }

        Category category = Category.builder()
                .categoryName(categoryRequest.getCategoryName())
                .description(categoryRequest.getDescription())
                .shelves(shelves)
                .build();
        categoryRepository.save(category);
        log.info("Created new category: {}", category.getCategoryName());

        return categoryMapper.toResponse(category);
    }

    public CategoryResponse updateCategory(Integer categoryId, CategoryRequest categoryRequest) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));

        Set<Shelf> shelves = new HashSet<>();
        for (Integer id : categoryRequest.getShelfIds()) {
            Shelf shelf = shelfRepository.findById(id).
                    orElseThrow(() -> new RuntimeException("Shelf not found with id: " + id));

            shelves.add(shelf);
        }
        category.setShelves(shelves);
        categoryMapper.updateCategory(category, categoryRequest);
        categoryRepository.save(category);

        log.info("Updated category: {}", category.getCategoryName());

        return categoryMapper.toResponse(category);
    }
}
