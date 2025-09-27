package com.example.librarymanagementwebsite.feature.shelves;

import com.example.librarymanagementwebsite.feature.category.Category;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "shelves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shelf_id")
    private Integer shelfId;

    @Column(nullable = false)
    private Integer floor;

    @Column(nullable = false, length = 20)
    private String code;

    @Column(nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "shelf", cascade = CascadeType.ALL)
    private Set<ShelfSlot> slots;

    @ManyToMany(mappedBy = "shelves")
    private Set<Category> categories;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}