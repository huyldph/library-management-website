package com.example.librarymanagementwebsite.feature.book.dto;

import lombok.Data;

@Data
public class BookResponse {
    private Integer bookId;
    private String title;
    private String isbn;
    private String author;
    private Integer publicationYear;
    private String description;
    private String publisherName;
    private String categoryName;
}
