package com.example.librarymanagementwebsite.feature.bookcopy;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Integer> {
    @Query("""
            SELECT bc FROM BookCopy bc
            JOIN FETCH bc.book b
            LEFT JOIN FETCH bc.slot s
            LEFT JOIN FETCH s.shelf sh
            WHERE b.bookId = :bookId
            """)
    Page<BookCopy> findByBookId(@Param("bookId") Integer bookId, Pageable pageable);
}
