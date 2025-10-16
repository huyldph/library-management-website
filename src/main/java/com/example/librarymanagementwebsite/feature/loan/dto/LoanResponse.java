package com.example.librarymanagementwebsite.feature.loan.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanResponse {
    private String id;
    private String memberId;
    private String bookId;
    private String bookCopyId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private Integer renewalCount;
    private Integer maxRenewals;
    private Double fineAmount;
    
    // Legacy fields for backward compatibility
    private String loanCode;
    private String bookTitle;
    private String memberName;
    private String loanDate;
}
