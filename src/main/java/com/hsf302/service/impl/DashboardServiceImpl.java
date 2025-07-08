package com.hsf302.service.impl;

import com.hsf302.enums.VerificationStatus;
import com.hsf302.repository.StudentVerificationRepository;
import com.hsf302.repository.UserRepository;
import com.hsf302.service.interfaces.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentVerificationRepository verificationRepository;

    @Override
    public long countPendingVerifications() {
        return verificationRepository.countByStatus(VerificationStatus.PENDING);
    }

    @Override
    public long countVerifiedStudents() {
        return userRepository.countByisStudentTrue();
    }

    @Override
    public long countTotalUsers() {
        return userRepository.count();
    }
}
