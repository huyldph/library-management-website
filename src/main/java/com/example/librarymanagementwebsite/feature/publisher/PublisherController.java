package com.example.librarymanagementwebsite.feature.publisher;

import com.example.librarymanagementwebsite.util.ApiResponse;
import com.example.librarymanagementwebsite.feature.publisher.dto.PublisherRequest;
import com.example.librarymanagementwebsite.feature.publisher.dto.PublisherResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/publishers")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class PublisherController {
    PublisherService publisherService;

    @GetMapping
    public ApiResponse<?> getPublishers(@RequestParam(defaultValue = "0") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size) {
        Page<PublisherResponse> publishers = publisherService.getPublishers(page, size);

        return ApiResponse.builder()
                .result(publishers)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PublisherResponse> getPublisherById(@PathVariable Integer id) {
        PublisherResponse publisher = publisherService.getPublisherById(id);

        return ApiResponse.<PublisherResponse>builder()
                .result(publisher)
                .build();
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<PublisherResponse> createPublisher(@RequestBody PublisherRequest request) {
        PublisherResponse publisher = publisherService.createPublisher(request);

        return ApiResponse.<PublisherResponse>builder()
                .result(publisher)
                .build();
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<PublisherResponse> updatePublisher(@RequestParam Integer id, @RequestBody PublisherRequest request) {
        PublisherResponse publisher = publisherService.updatePublisher(id, request);
        return ApiResponse.<PublisherResponse>builder()
                .result(publisher)
                .build();
    }
}
