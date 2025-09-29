package com.example.librarymanagementwebsite.feature.loan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}