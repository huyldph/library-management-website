package com.example.librarymanagementwebsite.feature.member.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberResponse {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String cardNumber;
    private LocalDate registrationDate;
    private LocalDate expiryDate;
}
