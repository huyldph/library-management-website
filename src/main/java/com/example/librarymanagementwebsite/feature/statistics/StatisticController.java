package com.example.librarymanagementwebsite.feature.statistics;

import com.example.librarymanagementwebsite.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/statistics")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class StatisticController {
    StatisticService statisticService;

    @GetMapping("/top-borrowed-books")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<?> getTopBorrowedBooks(
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ) {
        return ApiResponse.builder()
                .result(statisticService.getTopBorrowedBooks(
                        fromDate != null ? LocalDate.parse(fromDate) : null,
                        toDate != null ? LocalDate.parse(toDate) : null
                ))
                .build();
    }

    @GetMapping("/overdue-loans")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<?> getOverdueLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ) {
        return ApiResponse.builder()
                .result(statisticService.getOverdueLoans(
                        page, size,
                        fromDate != null ? LocalDate.parse(fromDate) : null,
                        toDate != null ? LocalDate.parse(toDate) : null
                ))
                .build();
    }

    @GetMapping("/member-activity")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<?> getMemberActivity(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate
    ) {
        return ApiResponse.builder()
                .result(statisticService.getMemberActivity(
                        page, size,
                        fromDate != null ? LocalDate.parse(fromDate) : null,
                        toDate != null ? LocalDate.parse(toDate) : null
                ))
                .build();
    }
}
