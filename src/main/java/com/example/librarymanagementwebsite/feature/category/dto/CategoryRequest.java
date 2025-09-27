package com.example.librarymanagementwebsite.feature.category.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryRequest {
    private String categoryName;
    private String description;
    private List<Integer> shelfIds;
}
