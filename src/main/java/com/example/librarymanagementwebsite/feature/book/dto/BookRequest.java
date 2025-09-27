package com.example.librarymanagementwebsite.feature.book.dto;

import lombok.Data;

@Data
public class BookRequest {
    private String title;
    private  String author;
    private Integer publicationYear;
    private String description;
    private Integer publisherId;
    private Integer categoryId;
}
