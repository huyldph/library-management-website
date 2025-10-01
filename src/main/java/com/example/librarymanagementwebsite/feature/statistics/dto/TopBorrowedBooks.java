package com.example.librarymanagementwebsite.feature.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class TopBorrowedBooks {
    private String title;
    private Long borrowedBooks;
}
