package com.example.librarymanagementwebsite.feature.loan;

import com.example.librarymanagementwebsite.constant.BookCopyStatus;
import com.example.librarymanagementwebsite.constant.FineStatus;
import com.example.librarymanagementwebsite.constant.LoanStatus;
import com.example.librarymanagementwebsite.constant.MemberStatus;
import com.example.librarymanagementwebsite.feature.bookcopy.BookCopy;
import com.example.librarymanagementwebsite.feature.bookcopy.BookCopyRepository;
import com.example.librarymanagementwebsite.feature.loan.dto.LoanResponse;
import com.example.librarymanagementwebsite.feature.loan.mapper.LoanMapper;
import com.example.librarymanagementwebsite.feature.member.Member;
import com.example.librarymanagementwebsite.feature.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class LoanService {
    LoanRepository loanRepository;
    LoanMapper loanMapper;
    MemberRepository memberRepository;
    BookCopyRepository bookCopyRepository;
    FineRepository fineRepository;

    public Page<LoanResponse> getLoansByMemberId(Integer memberId, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Loan> loans = loanRepository.findByMemberId(memberId, pageable);

        return loans.map(loanMapper::toResponse);
    }

    public Page<LoanResponse> getPublicLoansByMemberId(Integer memberId, int page, int size) {
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

    @Transactional
    public LoanResponse checkout(String cardNumber, String bookBarcode) {
        Member member = memberRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Member not found with card number: " + cardNumber));

        BookCopy bookCopy = bookCopyRepository.findByBarcode(bookBarcode)
                .orElseThrow(() -> new RuntimeException("Book copy not found with barcode: " + bookBarcode));

        if (member.getStatus().equals(MemberStatus.Active)
                && member.countBorrowedBooks() <= 3
                && bookCopy.getStatus().equals(BookCopyStatus.Available)) {
            Loan loan = Loan.builder()
                    .loanCode(generateLoanCode())
                    .member(member)
                    .bookCopy(bookCopy)
                    .loanDate(LocalDate.now())
                    .dueDate(LocalDate.now().plusDays(14))
                    .status(LoanStatus.Active)
                    .build();

            bookCopy.setStatus(BookCopyStatus.Loaned);
            bookCopyRepository.save(bookCopy);

            Loan savedLoan = loanRepository.save(loan);
            log.info("Book copy with barcode {} checked out to member with card number {}", bookBarcode, cardNumber);
            return loanMapper.toResponse(savedLoan);
        } else {
            throw new RuntimeException("Cannot checkout book. Either member is not active, has reached the borrow limit, or book copy is not available.");
        }
    }

    @Transactional
    public LoanResponse checkin(String bookBarcode) {
        BookCopy bookCopy = bookCopyRepository.findByBarcode(bookBarcode)
                .orElseThrow(() -> new RuntimeException("Book copy not found with barcode: " + bookBarcode));

        Loan loan = loanRepository.findActiveLoanByBookCopy_CopyId(bookCopy.getCopyId())
                .orElseThrow(() -> new RuntimeException("No active loan found for book copy with barcode: " + bookBarcode));

        loan.setReturnDate(LocalDate.now());
        if (LocalDate.now().isAfter(loan.getDueDate())) {
            loan.setStatus(LoanStatus.Overdue);

            // Create a fine for the overdue loan
            fines(loan);
        } else {
            loan.setStatus(LoanStatus.Returned);
        }

        bookCopy.setStatus(BookCopyStatus.Available);
        bookCopyRepository.save(bookCopy);

        Loan updatedLoan = loanRepository.save(loan);
        log.info("Book copy with barcode {} checked in", bookBarcode);
        return loanMapper.toResponse(updatedLoan);
    }

    private void fines(Loan loan) {
        Fine fine = Fine.builder()
                .loan(loan)
                .member(loan.getMember())
                .fineAmount(BigDecimal.valueOf(10000.0))
                .paidAmount(BigDecimal.ZERO)
                .status(FineStatus.Unpaid)
                .build();

        fineRepository.save(fine);
        log.info("Fine created for loan ID {}: {}", loan.getLoanId(), fine.getFineAmount());
    }

    private String generateLoanCode() {
        String prefix = "LN";
        long count = loanRepository.count() + 1;
        return String.format("%s%05d", prefix, count);
    }
}
