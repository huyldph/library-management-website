package com.example.librarymanagementwebsite.feature.account.mapper;

import com.example.librarymanagementwebsite.feature.account.User;
import com.example.librarymanagementwebsite.feature.account.dto.RegisterRequest;
import com.example.librarymanagementwebsite.feature.account.dto.RegisterResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "passwordHash", source = "password")
    User toEntity(RegisterRequest registerRequest);

    @Mapping(target = "password", source = "passwordHash")
    RegisterResponse toResponse(User user);
}
