package com.example.librarymanagementwebsite.feature.loan;

import com.example.librarymanagementwebsite.feature.loan.dto.LoanResponse;
import com.example.librarymanagementwebsite.util.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/public/{memberId}")
    ApiResponse<?> getPublicLoansByMemberId(
            @PathVariable Integer memberId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<LoanResponse> loans = loanService.getPublicLoansByMemberId(memberId, page, size);
        return ApiResponse.builder()
                .result(loans)
                .message("Success")
                .build();
    }

    @PostMapping("/checkout")
    ApiResponse<?> checkout(
            @RequestParam String cardNumber,
            @RequestParam String bookBarcode
    ) {
        LoanResponse loanResponse = loanService.checkout(cardNumber, bookBarcode);
        return ApiResponse.builder()
                .result(loanResponse)
                .message("Checkout book successfully")
                .build();
    }

    @PutMapping("/checkin")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    ApiResponse<?> checkin(
            @RequestParam String bookBarcode
    ) {
        LoanResponse loanResponse = loanService.checkin(bookBarcode);
        return ApiResponse.builder()
                .result(loanResponse)
                .message("Checkin book successfully")
                .build();
    }
}
