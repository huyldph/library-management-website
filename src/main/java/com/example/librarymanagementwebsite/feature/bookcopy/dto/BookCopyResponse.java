package com.example.librarymanagementwebsite.feature.bookcopy.dto;

import lombok.Data;

@Data
public class BookCopyResponse {
    private String title;
    private String isbn;
    private String author;
    private String publicationYear;
    private String barcode;
    private String barcodeImageUrl;
    private String status;
    private String location;
}
