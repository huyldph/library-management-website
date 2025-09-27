package com.example.librarymanagementwebsite.feature.shelves.dto;

import lombok.Data;

@Data
public class ShelfSlotResponse {
    private Integer position;
    private Boolean isOccupied;
}
