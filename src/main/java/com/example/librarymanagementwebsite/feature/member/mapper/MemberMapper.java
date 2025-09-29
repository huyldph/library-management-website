package com.example.librarymanagementwebsite.feature.member.mapper;

import com.example.librarymanagementwebsite.feature.member.Member;
import com.example.librarymanagementwebsite.feature.member.dto.MemberRequest;
import com.example.librarymanagementwebsite.feature.member.dto.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member toEntity(MemberRequest memberRequest);

    MemberResponse toResponse(Member member);

    void updateMember(MemberRequest memberRequest, @MappingTarget Member member);
}
