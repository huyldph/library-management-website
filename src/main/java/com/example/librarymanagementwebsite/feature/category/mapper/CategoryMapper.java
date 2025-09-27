package com.example.librarymanagementwebsite.feature.category.mapper;

import com.example.librarymanagementwebsite.feature.category.Category;
import com.example.librarymanagementwebsite.feature.category.dto.CategoryRequest;
import com.example.librarymanagementwebsite.feature.category.dto.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);

    void updateCategory(@MappingTarget Category category, CategoryRequest categoryRequest);
}
