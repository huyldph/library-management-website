package com.example.librarymanagementwebsite.feature.statistics;

import com.example.librarymanagementwebsite.feature.loan.LoanRepository;
import com.example.librarymanagementwebsite.feature.statistics.dto.MemberActivity;
import com.example.librarymanagementwebsite.feature.statistics.dto.OverdueLoans;
import com.example.librarymanagementwebsite.feature.statistics.dto.TopBorrowedBooks;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class StatisticService {
    LoanRepository loanRepository;

    //Top sách mượn nhiều nhất
    public List<TopBorrowedBooks> getTopBorrowedBooks(LocalDate fromDate, LocalDate toDate) {
        return loanRepository.findTopBorrowedBooks(fromDate, toDate);
    }

    //danh sách mượn quá hạn
    public Page<OverdueLoans> getOverdueLoans(int page, int size, LocalDate fromDate, LocalDate toDate) {
        Pageable pageable = PageRequest.of(page, size);

        return loanRepository.findOverdueLoans(pageable, fromDate, toDate);
    }

    //Thống số lượng sách mượn theo từng thành viên
    public Page<MemberActivity> getMemberActivity(int page, int size, LocalDate fromDate, LocalDate toDate) {
        Pageable pageable = PageRequest.of(page, size);

        return loanRepository.findMemberActivity(pageable, fromDate, toDate);
    }
}
