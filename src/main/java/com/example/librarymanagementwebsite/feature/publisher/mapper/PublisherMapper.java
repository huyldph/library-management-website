package com.example.librarymanagementwebsite.feature.publisher.mapper;

import com.example.librarymanagementwebsite.feature.publisher.Publisher;
import com.example.librarymanagementwebsite.feature.publisher.dto.PublisherRequest;
import com.example.librarymanagementwebsite.feature.publisher.dto.PublisherResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
    PublisherResponse toResponse(Publisher publisher);

    void updateEntity(PublisherRequest request, @MappingTarget Publisher publisher);
}
