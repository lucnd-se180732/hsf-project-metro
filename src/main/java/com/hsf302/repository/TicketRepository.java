package com.hsf302.repository;

import com.hsf302.pojo.Ticket;
import com.hsf302.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUser(User user);
    Page<Ticket>  findAllTicketsByUser(User user, Pageable pageable);
}
