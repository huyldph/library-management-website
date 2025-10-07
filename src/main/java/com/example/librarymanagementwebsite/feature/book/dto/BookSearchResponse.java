package com.example.librarymanagementwebsite.feature.book.dto;

import lombok.Data;


@Data
public class BookSearchResponse {
    private Integer bookId;
    private String title;
    private String isbn;
    private String author;
    private Integer publicationYear;
    private String description;
    private String imageUrl;
    private String publisherName;
    private String categoryName;
    private Long totalCopies;
    private Long availableCopies;
}
