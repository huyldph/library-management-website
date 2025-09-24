package com.example.librarymanagementwebsite.account.mapper;

import com.example.librarymanagementwebsite.account.User;
import com.example.librarymanagementwebsite.account.dto.RegisterRequest;
import com.example.librarymanagementwebsite.account.dto.RegisterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "passwordHash", source = "password")
    User toEntity(RegisterRequest registerRequest);

    @Mapping(target = "password", source = "passwordHash")
    RegisterResponse toResponse(User user);
}
