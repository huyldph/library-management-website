package com.example.librarymanagementwebsite.feature.member;

import com.example.librarymanagementwebsite.constant.MemberStatus;
import com.example.librarymanagementwebsite.feature.loan.Loan;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "card_number", length = 50, nullable = false, unique = true)
    private String cardNumber;

    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private MemberStatus status;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Loan> loans;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (cardNumber == null || cardNumber.isEmpty()) {
            cardNumber = renderCardNumber();
        }
        if (status == null) {
            status = MemberStatus.Active;
        }
        if (registrationDate == null) {
            registrationDate = LocalDate.now();
        }
        if (expiryDate == null) {
            expiryDate = registrationDate.plusYears(1);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updateStatus();
    }

    private String renderCardNumber() {
        int randomPart = new Random().nextInt(9000) + 1000;

        return "CARD-" + randomPart;
    }

    // Check thẻ hết hạn
    public boolean isCardExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }

    // Đếm số sách đang mượn
    public long countBorrowedBooks() {
        if (loans == null) return 0;
        return loans.stream()
                .filter(loan -> "Active".equalsIgnoreCase(String.valueOf(loan.getStatus())))
                .count();
    }

    // Đếm số lần trả muộn
    private long countLateReturns() {
        if (loans == null) return 0;
        return loans.stream()
                .filter(loan -> loan.getReturnDate() != null
                        && loan.getDueDate() != null
                        && loan.getReturnDate().isAfter(loan.getDueDate()))
                .count();
    }

    // Cập nhật trạng thái thẻ
    private void updateStatus() {
        if (isCardExpired()) {
            this.status = MemberStatus.Inactive;
        } else if (countLateReturns() > 3) {
            this.status = MemberStatus.Suspended;
        } else {
            this.status = MemberStatus.Active;
        }
    }
}
