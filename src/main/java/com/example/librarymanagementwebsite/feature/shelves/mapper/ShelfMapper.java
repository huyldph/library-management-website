package com.example.librarymanagementwebsite.feature.shelves.mapper;

import com.example.librarymanagementwebsite.feature.shelves.Shelf;
import com.example.librarymanagementwebsite.feature.shelves.dto.ShelfRequest;
import com.example.librarymanagementwebsite.feature.shelves.dto.ShelfResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ShelfMapper {
    ShelfResponse toResponse(Shelf shelf);

    Shelf toEntity(ShelfRequest shelfRequest);

    void updateEntityFromRequest(ShelfRequest shelfRequest, @MappingTarget Shelf shelf);
}
