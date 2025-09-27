package com.example.librarymanagementwebsite.feature.shelves;

import com.example.librarymanagementwebsite.util.ApiResponse;
import com.example.librarymanagementwebsite.feature.shelves.dto.ShelfRequest;
import com.example.librarymanagementwebsite.feature.shelves.dto.ShelfResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shelves")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ShelfController {
    ShelfService shelfService;

    @GetMapping("/{shelfId}/slots")
    public ApiResponse<?> getShelfSlotsByShelfId(@PathVariable Integer shelfId,
                                                 @RequestParam(defaultValue = "0") Integer page,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        var shelfSlots = shelfService.getShelfSlotByShelfId(shelfId, page, size);
        return ApiResponse.builder()
                .result(shelfSlots)
                .build();
    }

    @GetMapping
    public ApiResponse<?> getAllShelves(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        var shelves = shelfService.getAllShelves(page, size);

        return ApiResponse.builder()
                .result(shelves)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ShelfResponse> getShelfById(@PathVariable Integer id) {
        ShelfResponse shelfResponse = shelfService.getShelfById(id);

        return ApiResponse.<ShelfResponse>builder()
                .result(shelfResponse)
                .build();
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<?> createShelf(@RequestBody ShelfRequest request) {
        ShelfResponse shelfResponse = shelfService.createShelf(request);

        return ApiResponse.builder()
                .result(shelfResponse)
                .build();
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<?> updateShelf(@RequestParam Integer id, @RequestBody ShelfRequest request) {
        ShelfResponse shelfResponse = shelfService.updateShelf(id, request);

        return ApiResponse.builder()
                .result(shelfResponse)
                .build();
    }
}
