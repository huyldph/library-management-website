package com.example.librarymanagementwebsite.feature.member;

import com.example.librarymanagementwebsite.feature.member.dto.MemberRequest;
import com.example.librarymanagementwebsite.feature.member.dto.MemberResponse;
import com.example.librarymanagementwebsite.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MemberController {
    MemberService memberService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<?> getMembers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<MemberResponse> members = memberService.getMembers(page, size);

        return ApiResponse.builder()
                .result(members)
                .message("Members retrieved successfully")
                .build();
    }

    @GetMapping("/{memberId}")
    public ApiResponse<?> getMemberById(@PathVariable Integer memberId) {
        MemberResponse memberResponse = memberService.getMemberById(memberId);
        return ApiResponse.builder()
                .result(memberResponse)
                .message("Member retrieved successfully")
                .build();
    }

    @GetMapping("/card-number/{cardNumber}")
    public ApiResponse<?> getMemberByCardNumber(@PathVariable String cardNumber) {
        MemberResponse memberResponse = memberService.getMemberByCardNumber(cardNumber);
        return ApiResponse.builder()
                .result(memberResponse)
                .message("Member retrieved successfully")
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<?> createMember(@Valid @RequestBody MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.createMember(memberRequest);

        return ApiResponse.builder()
                .result(memberResponse)
                .message("Member created successfully")
                .build();
    }

    @PutMapping("/update")
    public ApiResponse<?> updateMember(
            @RequestParam("memberId") Integer memberId,
            @Valid @RequestBody MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.updateMember(memberId, memberRequest);

        return ApiResponse.builder()
                .result(memberResponse)
                .message("Member updated successfully")
                .build();
    }
}
