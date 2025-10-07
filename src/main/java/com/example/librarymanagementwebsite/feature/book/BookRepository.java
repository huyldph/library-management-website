package com.example.librarymanagementwebsite.feature.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("""
            SELECT b FROM Book b
            WHERE (:category IS NULL OR LOWER(b.category.categoryName) = LOWER(:category) OR :category = 'all')
              AND (
                   :query IS NULL OR :query = ''
                   OR LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%'))
                   OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%'))
                   OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :query, '%'))
                   OR LOWER(b.description) LIKE LOWER(CONCAT('%', :query, '%'))
              )
            """)
    Page<Book> searchBooks(@Param("query") String query,
                           @Param("category") String category,
                           Pageable pageable);
}
