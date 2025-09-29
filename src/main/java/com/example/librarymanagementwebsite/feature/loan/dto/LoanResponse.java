package com.example.librarymanagementwebsite.feature.loan.dto;

import lombok.Data;

@Data
public class LoanResponse {
    private String loanCode;
    private String bookTitle;
    private String memberName;
    private String loanDate;
    private String returnDate;
    private String status;
}
