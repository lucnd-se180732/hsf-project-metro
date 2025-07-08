package com.hsf302.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class StudentVerificationDTO {
    private String fullName;
    private String idNumber;
    private String studentCode;
    private LocalDate dob;
    private String university;
    private MultipartFile studentCardFront;
    private MultipartFile studentCardBack;
}
