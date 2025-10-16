package com.example.librarymanagementwebsite.feature.member.mapper;

import com.example.librarymanagementwebsite.feature.member.Member;
import com.example.librarymanagementwebsite.feature.member.dto.MemberRequest;
import com.example.librarymanagementwebsite.feature.member.dto.MemberResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member toEntity(MemberRequest memberRequest);

    @Mapping(target = "id", source = "memberId")
    @Mapping(target = "memberCode", source = "cardNumber")
    @Mapping(target = "membershipStatus", source = "status")
    @Mapping(target = "currentBorrowCount", expression = "java((int) member.countBorrowedBooks())")
    @Mapping(target = "maxBorrowLimit", constant = "5")
    MemberResponse toResponse(Member member);

    void updateMember(MemberRequest memberRequest, @MappingTarget Member member);
}
