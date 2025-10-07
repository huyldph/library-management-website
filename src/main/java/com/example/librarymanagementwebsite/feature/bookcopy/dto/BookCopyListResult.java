package com.example.librarymanagementwebsite.feature.bookcopy.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookCopyListResult {
    private List<BookCopyResponse> items;
    private Long total;
}


