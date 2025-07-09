package com.hsf302.service.impl;

import com.hsf302.dto.StudentVerificationDTO;
import com.hsf302.enums.VerificationStatus;
import com.hsf302.pojo.StudentVerification;
import com.hsf302.pojo.User;
import com.hsf302.repository.StudentVerificationRepository;
import com.hsf302.repository.UserRepository;
import com.hsf302.service.interfaces.StudentVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentVerificationServiceImpl implements StudentVerificationService {

    @Autowired
    private StudentVerificationRepository verificationRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public void handleUpload(StudentVerificationDTO request, String username) throws IOException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng: " + username));

        String uploadDir = System.getProperty("user.dir") + "/uploads/verify";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String baseFilename = username.replaceAll("[^a-zA-Z0-9]", "_");
        String frontFilename = baseFilename + "-front.jpg";
        String backFilename = baseFilename + "-back.jpg";

        Path frontPath = uploadPath.resolve(frontFilename);
        Path backPath = uploadPath.resolve(backFilename);

        request.getStudentCardFront().transferTo(frontPath.toFile());
        request.getStudentCardBack().transferTo(backPath.toFile());

        String frontUrl = "/uploads/verify/" + frontFilename;
        String backUrl = "/uploads/verify/" + backFilename;

        StudentVerification verification = new StudentVerification();

        verification.setUser(user);
        verification.setFullName(request.getFullName().trim());
        verification.setIdNumber(request.getIdNumber().trim());
        verification.setStudentCode(request.getStudentCode().trim());
        verification.setDob(request.getDob());
        verification.setUniversity(request.getUniversity().trim());

        verification.setStudentCardFrontImageUrl(frontUrl);
        verification.setStudentCardBackImageUrl(backUrl);
        verification.setSubmittedAt(LocalDateTime.now());
        verification.setStatus(VerificationStatus.PENDING);

        verificationRepository.save(verification);
    }


    @Override
    public List<StudentVerification> searchVerifications(String fullName, String studentCode, String status) {
        List<StudentVerification> verifications = verificationRepository.findAll();

        return verifications.stream()
                .filter(v -> fullName == null || fullName.isBlank() ||
                        v.getUser().getFullName().toLowerCase().contains(fullName.toLowerCase()))
                .filter(v -> studentCode == null || studentCode.isBlank() ||
                        v.getStudentCode().toLowerCase().contains(studentCode.toLowerCase()))
                .filter(v -> {
                    if (status == null || status.isBlank()) return true;
                    try {
                        return v.getStatus().equals(VerificationStatus.valueOf(status));
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public long countByStatus(VerificationStatus status) {
        return verificationRepository.countByStatus(status);
    }

    @Override
    @Transactional
    public void approveVerification(Long id) {
        verificationRepository.findById(id).ifPresent(verification -> {
            verification.setStatus(VerificationStatus.APPROVED);
            verificationRepository.save(verification);

            User user = verification.getUser();
            user.setStudent(true);
            user.setIdentificationNumber(verification.getIdNumber());
            userRepository.save(user);
        });
    }

    @Override
    @Transactional
    public void rejectVerification(Long id) {
        verificationRepository.findById(id).ifPresent(verification -> {
            verification.setStatus(VerificationStatus.REJECTED);
            verificationRepository.save(verification);
        });
    }


    @Override
    public boolean hasPendingOrApprovedRequest(Long userId) {
        Optional<StudentVerification> latest = verificationRepository.findTopByUserIdOrderBySubmittedAtDesc(userId);
        return latest.isPresent() && latest.get().getStatus() != VerificationStatus.REJECTED;
    }

    @Override
    public boolean isIdNumberExistsForOther(String idNumber, Long currentUserId) {
        return verificationRepository.existsByIdNumberAndUserIdNotAndStatus(
                idNumber, currentUserId, VerificationStatus.APPROVED
        );
    }

    @Override
    public Optional<StudentVerification> findLatestByUserId(Long userId) {
        return verificationRepository.findTopByUserIdOrderBySubmittedAtDesc(userId);
    }



}
