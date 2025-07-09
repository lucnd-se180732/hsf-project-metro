package com.hsf302.repository;

import com.hsf302.enums.VerificationStatus;
import com.hsf302.pojo.StudentVerification;
import com.hsf302.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentVerificationRepository extends JpaRepository<StudentVerification, Long> {
    Optional<StudentVerification> findTopByUserOrderBySubmittedAtDesc(User user);

    Optional<StudentVerification> findByUser(User user);

    Long countByStatus(VerificationStatus status);

    List<StudentVerification> findByStatus(VerificationStatus status);

    Optional<StudentVerification> findTopByUserIdOrderBySubmittedAtDesc(Long userId);

    boolean existsByIdNumberAndUserIdNot(String idNumber, Long userId);


    List<StudentVerification> findByUserFullNameContainingIgnoreCaseAndStudentCodeContainingIgnoreCaseAndStatus(
            String fullName, String studentCode, VerificationStatus status);

    List<StudentVerification> findByUserFullNameContainingIgnoreCaseAndStudentCodeContainingIgnoreCase(
            String fullName, String studentCode);

    List<StudentVerification> findByUserFullNameContainingIgnoreCase(String fullName);

    List<StudentVerification> findByStudentCodeContainingIgnoreCase(String studentCode);

    boolean existsByIdNumberAndUserIdNotAndStatus(String idNumber, Long userId, VerificationStatus status);



}
