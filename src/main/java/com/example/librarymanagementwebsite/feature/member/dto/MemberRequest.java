package com.example.librarymanagementwebsite.feature.member.dto;

import lombok.Data;

import jakarta.validation.constraints.*;

@Data
public class MemberRequest {
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    private String fullName;

    @Email(message = "Email is not valid")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email format is invalid"
    )
    private String email;

    @Pattern(regexp = "^(\\+84|0)\\d{9,10}$", message = "Phone number is not valid")
    private String phoneNumber;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;
}