package com.example.librarymanagementwebsite.feature.member.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberResponse {
    private String id;
    private String memberCode;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String cardNumber;
    private String membershipStatus;
    private Integer currentBorrowCount;
    private Integer maxBorrowLimit;
    private LocalDate registrationDate;
    private LocalDate expiryDate;
}
