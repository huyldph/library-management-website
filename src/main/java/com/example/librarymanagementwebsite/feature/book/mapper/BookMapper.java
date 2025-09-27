package com.example.librarymanagementwebsite.feature.book.mapper;

import com.example.librarymanagementwebsite.feature.book.Book;
import com.example.librarymanagementwebsite.feature.book.dto.BookRequest;
import com.example.librarymanagementwebsite.feature.book.dto.BookResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper {
    @Mapping(target = "bookId", ignore = true)
    Book toEntity(BookRequest request);

    @Mapping(target = "publisherName", source = "publisher.publisherName")
    @Mapping(target = "categoryName", source = "category.categoryName")
    BookResponse toResponse(Book book);

    void updateBook(BookRequest request, @MappingTarget Book book);
}
