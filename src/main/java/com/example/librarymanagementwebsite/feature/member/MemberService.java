package com.example.librarymanagementwebsite.feature.member;

import com.example.librarymanagementwebsite.feature.member.dto.MemberRequest;
import com.example.librarymanagementwebsite.feature.member.dto.MemberResponse;
import com.example.librarymanagementwebsite.feature.member.mapper.MemberMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    MemberRepository memberRepository;
    MemberMapper memberMapper;

    public MemberResponse getMemberById(Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));

        return memberMapper.toResponse(member);
    }

    public MemberResponse getMemberByCardNumber(String cardNumber) {
        Member member = memberRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + cardNumber));

        return memberMapper.toResponse(member);
    }

    public Page<MemberResponse> getMembers(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Member> members = memberRepository.findAll(pageable);

        return members.map(memberMapper::toResponse);
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberMapper.toEntity(memberRequest);
        memberRepository.save(member);

        log.info("Member created with ID: {}", member.getMemberId());

        return memberMapper.toResponse(member);
    }

    public MemberResponse updateMember(Integer memberId, MemberRequest memberRequest) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with ID: " + memberId));

        memberMapper.updateMember(memberRequest, member);
        memberRepository.save(member);

        log.info("Member updated with ID: {}", member.getMemberId());

        return memberMapper.toResponse(member);
    }
}
