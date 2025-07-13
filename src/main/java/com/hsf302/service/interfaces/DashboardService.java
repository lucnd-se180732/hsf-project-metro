package com.hsf302.service.interfaces;

public interface DashboardService {
    long countPendingVerifications();
    long countVerifiedStudents();
    long countTotalUsers();
}
