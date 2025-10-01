package com.example.librarymanagementwebsite.feature.loan;

import com.example.librarymanagementwebsite.feature.statistics.dto.MemberActivity;
import com.example.librarymanagementwebsite.feature.statistics.dto.OverdueLoans;
import com.example.librarymanagementwebsite.feature.statistics.dto.TopBorrowedBooks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Integer> {

    @Query("""
                    SELECT l FROM Loan l
                    JOIN FETCH l.bookCopy bc
                    JOIN FETCH bc.book b
                    JOIN FETCH l.member m
                    WHERE m.memberId = :memberId
            """)
    Page<Loan> findByMemberId(@Param("memberId") Integer memberId, Pageable pageable);

    Optional<Loan> findActiveLoanByBookCopy_CopyId(Integer bookCopyCopyId);

    @Query("""
                SELECT new com.example.librarymanagementwebsite.feature.statistics.dto.TopBorrowedBooks(
                    b.title,
                    COUNT(l.loanId)
                )
                FROM Loan l
                    JOIN l.bookCopy bc
                    JOIN bc.book b
                WHERE l.returnDate IS NOT NULL
                  AND (:fromDate IS NULL OR l.loanDate >= :fromDate)
                  AND (:toDate IS NULL OR l.loanDate <= :toDate)
                GROUP BY b.bookId, b.title
                ORDER BY COUNT(l.loanId) DESC
            """)
    List<TopBorrowedBooks> findTopBorrowedBooks(@Param("fromDate") LocalDate fromDate,
                                                @Param("toDate") LocalDate toDate);


    @Query("""
                SELECT new com.example.librarymanagementwebsite.feature.statistics.dto.OverdueLoans(
                    m.fullName,
                    m.cardNumber,
                    b.title,
                    b.isbn,
                    b.author,
                    TO_CHAR(l.dueDate, 'DD-MM-YYYY')
                )
                FROM Loan l
                    JOIN l.member m
                    JOIN l.bookCopy bc
                    JOIN bc.book b
                WHERE (l.returnDate IS NULL AND l.dueDate < CURRENT_DATE)
                  AND (:fromDate IS NULL OR l.dueDate >= :fromDate)
                  AND (:toDate IS NULL OR l.dueDate <= :toDate)
                ORDER BY l.dueDate ASC
            """)
    Page<OverdueLoans> findOverdueLoans(Pageable pageable,
                                        @Param("fromDate") LocalDate fromDate,
                                        @Param("toDate") LocalDate toDate);


    @Query("""
                SELECT new com.example.librarymanagementwebsite.feature.statistics.dto.MemberActivity(
                    m.fullName,
                    m.cardNumber,
                    COUNT(l.loanId)
                )
                FROM Loan l
                    JOIN l.member m
                WHERE (:fromDate IS NULL OR l.loanDate >= :fromDate)
                  AND (:toDate IS NULL OR l.loanDate <= :toDate)
                GROUP BY m.memberId, m.fullName, m.cardNumber
                ORDER BY COUNT(l.loanId) DESC
            """)
    Page<MemberActivity> findMemberActivity(Pageable pageable,
                                            @Param("fromDate") LocalDate fromDate,
                                            @Param("toDate") LocalDate toDate);
}