package com.example.librarymanagementwebsite.feature.publisher;

import com.example.librarymanagementwebsite.feature.publisher.dto.PublisherRequest;
import com.example.librarymanagementwebsite.feature.publisher.dto.PublisherResponse;
import com.example.librarymanagementwebsite.feature.publisher.mapper.PublisherMapper;
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
public class PublisherService {
    PublisherRepository publisherRepository;
    PublisherMapper publisherMapper;

    public PublisherResponse getPublisherById(Integer publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + publisherId));

        return publisherMapper.toResponse(publisher);
    }

    public Page<PublisherResponse> getPublishers(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Publisher> publisherPage = publisherRepository.findAll(pageable);

        return publisherPage.map(publisherMapper::toResponse);
    }

    public PublisherResponse createPublisher(PublisherRequest request) {
        Publisher publisher = Publisher.builder()
                .publisherName(request.getPublisherName())
                .build();
        publisherRepository.save(publisher);

        return publisherMapper.toResponse(publisher);
    }

    public PublisherResponse updatePublisher(Integer publisherId, PublisherRequest request) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new RuntimeException("Publisher not found with id: " + publisherId));
        publisherMapper.updateEntity(request, publisher);
        publisherRepository.save(publisher);

        return publisherMapper.toResponse(publisher);
    }
}
