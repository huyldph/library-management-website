package com.example.librarymanagementwebsite.feature.shelves.dto;

import lombok.Data;

@Data
public class ShelfRequest {
    private Integer floor;
    private String code;
    private Integer capacity;
}
