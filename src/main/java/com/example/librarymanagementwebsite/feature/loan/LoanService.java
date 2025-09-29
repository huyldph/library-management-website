package com.example.librarymanagementwebsite.feature.loan;

import com.example.librarymanagementwebsite.feature.loan.dto.LoanResponse;
import com.example.librarymanagementwebsite.feature.loan.mapper.LoanMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class LoanService {
    LoanRepository loanRepository;
    LoanMapper loanMapper;

    public Page<LoanResponse> getLoansByMemberId(Integer memberId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Loan> loans = loanRepository.findByMemberId(memberId, pageable);

        return loans.map(loanMapper::toResponse);
    }

    public Page<LoanResponse> getLoans(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Loan> loans = loanRepository.findAll(pageable);

        return loans.map(loanMapper::toResponse);
    }
}
