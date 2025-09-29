package com.example.librarymanagementwebsite.feature.loan;

import com.example.librarymanagementwebsite.feature.loan.dto.LoanResponse;
import com.example.librarymanagementwebsite.util.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/loans")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LoanController {
    LoanService loanService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    ApiResponse<?> getLoans(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<LoanResponse> loans = loanService.getLoans(page, size);

        return ApiResponse.builder()
                .result(loans)
                .message("Get loans successfully")
                .build();
    }

    @GetMapping("/by-member")
    ApiResponse<?> getLoansByMemberId(
            @RequestParam Integer memberId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<LoanResponse> loans = loanService.getLoansByMemberId(memberId, page, size);
        return ApiResponse.builder()
                .result(loans)
                .message("Get loans for member with id: " + memberId)
                .build();
    }
}
