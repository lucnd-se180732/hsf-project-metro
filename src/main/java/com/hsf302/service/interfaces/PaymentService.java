package com.hsf302.service.interfaces;


import com.hsf302.pojo.Payment;
import com.hsf302.pojo.Ticket;
import com.hsf302.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.Optional;

public interface PaymentService {
    String generatePaymentUrl(Ticket ticket);
    boolean verifyPayment(Map<String, String> vnpParams);
    Page<Payment> getPaymentsByUser(User user, Pageable pageable);
    void save(Payment payment);
    Optional<Payment> findByTicket(Ticket ticket);
}