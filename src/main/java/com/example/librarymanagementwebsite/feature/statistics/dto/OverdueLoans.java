package com.example.librarymanagementwebsite.feature.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OverdueLoans {
    String fullName;
    String cardNumber;
    String title;
    String isbn;
    String author;
    String dueDate;
}
