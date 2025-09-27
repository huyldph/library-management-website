package com.example.librarymanagementwebsite.feature.bookcopy;

import com.example.librarymanagementwebsite.constant.BookCopyStatus;
import com.example.librarymanagementwebsite.feature.book.Book;
import com.example.librarymanagementwebsite.feature.shelves.ShelfSlot;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_copies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "copy_id")
    private Integer copyId;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false, referencedColumnName = "book_id")
    private Book book;

    @Column(nullable = false, unique = true, length = 50)
    private String barcode;

    @Column(name = "barcode_image_url")
    private String barcodeImageUrl;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private BookCopyStatus status = BookCopyStatus.Available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id")
    private ShelfSlot slot;  // vị trí trên kệ

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = BookCopyStatus.Available;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
