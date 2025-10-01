package com.example.librarymanagementwebsite.feature.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberActivity {
    private String fullName;
    private String cardNumber;
    private Long borrowedBooksCount;
}
