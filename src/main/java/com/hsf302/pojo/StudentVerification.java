package com.hsf302.pojo;

import com.hsf302.enums.VerificationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "student_verifications")
public class StudentVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = ("NVARCHAR(100)"))
    private String fullName;
    private String idNumber;
    private String studentCode;
    private LocalDate dob;
    @Column(columnDefinition = ("NVARCHAR(100)"))
    private String university;

    private String studentCardFrontImageUrl;
    private String studentCardBackImageUrl;


    private LocalDateTime submittedAt;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status = VerificationStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String adminNote;
}
