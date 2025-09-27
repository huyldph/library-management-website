package com.example.librarymanagementwebsite.feature.shelves.mapper;

import com.example.librarymanagementwebsite.feature.shelves.ShelfSlot;
import com.example.librarymanagementwebsite.feature.shelves.dto.ShelfSlotResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShelfSlotMapper {
    ShelfSlotResponse toResponse(ShelfSlot shelfSlot);
}
