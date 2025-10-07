package com.example.librarymanagementwebsite.feature.book.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookListResult {
    private List<BookSearchResponse> items;
    private Long total;
    private List<String> categories; // optional
}


