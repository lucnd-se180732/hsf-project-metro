package com.hsf302.repository;

import com.hsf302.pojo.Payment;
import com.hsf302.pojo.Ticket;
import com.hsf302.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Page<Payment> findAllByUser(User user, Pageable pageable);
    Optional<Payment> findByTicket(Ticket ticket);

}
