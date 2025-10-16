package com.example.librarymanagementwebsite.feature.loan.mapper;

import com.example.librarymanagementwebsite.feature.loan.Loan;
import com.example.librarymanagementwebsite.feature.loan.dto.LoanResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    @Mapping(target = "id", source = "loanId")
    @Mapping(target = "memberId", source = "member.memberId")
    @Mapping(target = "bookId", source = "bookCopy.book.bookId")
    @Mapping(target = "bookCopyId", source = "bookCopy.copyId")
    @Mapping(target = "borrowDate", source = "loanDate")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "renewalCount", constant = "0")
    @Mapping(target = "maxRenewals", constant = "2")
    @Mapping(target = "fineAmount", constant = "0.0")
    @Mapping(target = "bookTitle", source = "bookCopy.book.title")
    @Mapping(target = "memberName", source = "member.fullName")
    @Mapping(target = "loanCode", source = "loanCode")
    @Mapping(target = "loanDate", source = "loanDate")
    LoanResponse toResponse(Loan loan);
}
