package com.hsf302.service.interfaces;

import com.hsf302.enums.TicketType;
import com.hsf302.pojo.Ticket;
import com.hsf302.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface TicketService {
    Ticket previewTicket(TicketType type);
    Ticket previewSingleTicket(String from, String to);
    boolean validateStations(String from, String to);
    Ticket createTicket(TicketType type, User user);  // Add this method
    Page<Ticket> getTicketsByUser(User user, Pageable pageable);
    Ticket saveTicket(Ticket ticket);
    Optional<Ticket> getTicketById(Long ticketId);
}
