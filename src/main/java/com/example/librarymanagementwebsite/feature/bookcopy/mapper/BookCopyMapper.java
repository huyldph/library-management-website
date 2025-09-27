package com.example.librarymanagementwebsite.feature.bookcopy.mapper;

import com.example.librarymanagementwebsite.feature.bookcopy.BookCopy;
import com.example.librarymanagementwebsite.feature.bookcopy.dto.BookCopyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookCopyMapper {
    @Mapping(target = "title", source = "book.title")
    @Mapping(target = "isbn", source = "book.isbn")
    @Mapping(target = "author", source = "book.author")
    @Mapping(target = "publicationYear", source = "book.publicationYear")
    @Mapping(target = "location", expression = "java(formatLocation(bookCopy))")
    BookCopyResponse toResponse(BookCopy bookCopy);

    default String formatLocation(BookCopy bookCopy) {
        if (bookCopy.getSlot() == null) return null;

        var slot = bookCopy.getSlot();
        var shelf = slot.getShelf();

        return "Tầng " + shelf.getFloor() + " - "
                + shelf.getCode() + " - "
                + slot.getPosition();
    }
}