package com.example.librarymanagementwebsite.feature.shelves.dto;

import lombok.Data;

@Data
public class ShelfResponse {
    private Integer shelfId;
    private Integer floor;
    private String code;
    private Integer capacity;
}
