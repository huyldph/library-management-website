package com.example.librarymanagementwebsite.constant;

import lombok.Getter;

@Getter
public enum BookCopyStatus {
    Available,
    Loaned,
    Lost,
    Damaged,
    Reserved
}
