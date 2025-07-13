package com.hsf302.service.interfaces;

import com.hsf302.dto.StudentVerificationDTO;
import com.hsf302.enums.VerificationStatus;
import com.hsf302.pojo.StudentVerification;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface StudentVerificationService {
    void handleUpload(StudentVerificationDTO request, String username) throws IOException;

    List<StudentVerification> searchVerifications(String fullName, String studentCode, String status);

    public long countByStatus(VerificationStatus status);

    void approveVerification(Long id);

    void rejectVerification(Long id);

    boolean hasPendingOrApprovedRequest(Long userId);

    boolean isIdNumberExistsForOther(String idNumber, Long userId);

    Optional<StudentVerification> findLatestByUserId(Long userId);

}
