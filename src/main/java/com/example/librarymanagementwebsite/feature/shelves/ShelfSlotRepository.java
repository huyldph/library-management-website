package com.example.librarymanagementwebsite.feature.shelves;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelfSlotRepository extends JpaRepository<ShelfSlot, Integer> {
    Page<ShelfSlot> findByShelf_ShelfIdOrderByPositionAsc(Integer shelfId, Pageable pageable);
}
