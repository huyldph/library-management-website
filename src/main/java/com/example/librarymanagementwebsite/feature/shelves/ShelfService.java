package com.example.librarymanagementwebsite.feature.shelves;

import com.example.librarymanagementwebsite.feature.shelves.dto.ShelfRequest;
import com.example.librarymanagementwebsite.feature.shelves.dto.ShelfResponse;
import com.example.librarymanagementwebsite.feature.shelves.dto.ShelfSlotResponse;
import com.example.librarymanagementwebsite.feature.shelves.mapper.ShelfMapper;
import com.example.librarymanagementwebsite.feature.shelves.mapper.ShelfSlotMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ShelfService {
    ShelfRepository shelfRepository;
    ShelfSlotRepository shelfSlotRepository;
    ShelfMapper shelfMapper;
    ShelfSlotMapper shelfSlotMapper;

    public Page<ShelfSlotResponse> getShelfSlotByShelfId(Integer shelfId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ShelfSlot> shelfSlots = shelfSlotRepository.findByShelf_ShelfIdOrderByPositionAsc(shelfId, pageable);

        return shelfSlots.map(shelfSlotMapper::toResponse);
    }

    public Page<ShelfResponse> getAllShelves(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Page<Shelf> shelves = shelfRepository.findAll(PageRequest.of(page, size, sort));

        return shelves.map(shelfMapper::toResponse);
    }

    public ShelfResponse getShelfById(Integer id) {
        Shelf shelf = shelfRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shelf not found with ID: " + id));

        return shelfMapper.toResponse(shelf);
    }

    public ShelfResponse createShelf(ShelfRequest shelfRequest) {
        Shelf shelf = shelfMapper.toEntity(shelfRequest);
        Shelf savedShelf = shelfRepository.save(shelf);

        log.info("Created new shelf with ID: {}", savedShelf.getShelfId());

        return shelfMapper.toResponse(savedShelf);
    }

    public ShelfResponse updateShelf(Integer shelfId, ShelfRequest shelfRequest) {
        Shelf shelf = shelfRepository.findById(shelfId)
                .orElseThrow(() -> new RuntimeException("Shelf not found with ID: " + shelfId));

        shelfMapper.updateEntityFromRequest(shelfRequest, shelf);
        Shelf updatedShelf = shelfRepository.save(shelf);

        log.info("Updated shelf with ID: {}", updatedShelf.getShelfId());

        return shelfMapper.toResponse(updatedShelf);
    }
}
