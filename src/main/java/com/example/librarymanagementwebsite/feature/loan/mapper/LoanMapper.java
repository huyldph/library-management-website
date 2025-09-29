package com.example.librarymanagementwebsite.feature.loan.mapper;

import com.example.librarymanagementwebsite.feature.loan.Loan;
import com.example.librarymanagementwebsite.feature.loan.dto.LoanResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    @Mapping(target = "bookTitle", source = "bookCopy.book.title")
    @Mapping(target = "memberName", source = "member.fullName")
    LoanResponse toResponse(Loan loan);
}
