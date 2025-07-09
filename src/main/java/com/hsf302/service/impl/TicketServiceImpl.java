package com.hsf302.service.impl;

import com.hsf302.enums.TicketType;
import com.hsf302.pojo.Station;
import com.hsf302.pojo.Ticket;
import com.hsf302.pojo.TicketConfig;
import com.hsf302.pojo.User;
import com.hsf302.repository.StationRepository;
import com.hsf302.repository.TicketConfigRepository;
import com.hsf302.repository.TicketRepository;
import com.hsf302.service.interfaces.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private TicketConfigRepository ticketConfigRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Ticket previewTicket(TicketType type) {
        TicketConfig config = ticketConfigRepository.findByTicketType(type);

        Ticket ticket = new Ticket();
        ticket.setTicketType(type);
        ticket.setStartDate(LocalDateTime.now());
        ticket.setActivatedAt(null);         // ✅ Chưa kích hoạt
        ticket.setUsed(false);               // ✅ Chưa sử dụng
        ticket.setPrice(config.getPrice());
        ticket.setDescription(config.getDescription());
        ticket.setNote(config.getNote());

        switch (type) {
            case ONE_DAY -> ticket.setEndDate(LocalDateTime.now().plusDays(1));
            case THREE_DAY -> ticket.setEndDate(LocalDateTime.now().plusDays(3));
            case MONTHLY -> ticket.setEndDate(LocalDateTime.now().plusMonths(1));
            default -> ticket.setEndDate(LocalDateTime.now());
        }

        return ticket;
    }

    @Override
    public Ticket previewSingleTicket(String from, String to) {
        if (!validateStations(from, to)) {
            throw new IllegalArgumentException("Invalid stations");
        }

        TicketConfig config = ticketConfigRepository.findByTicketType(TicketType.SINGLE);

        Ticket ticket = new Ticket();
        ticket.setTicketType(TicketType.SINGLE);
        ticket.setStartDate(LocalDateTime.now());
        ticket.setEndDate(LocalDateTime.now().plusDays(30));  // ✅ vé lượt có hạn dùng 30 ngày
        ticket.setActivatedAt(null);
        ticket.setUsed(false);
        ticket.setPrice(config.getPrice());
        ticket.setDepartureStation(from);
        ticket.setArrivalStation(to);
        ticket.setDescription(
                config.getDescription()
                        .replace("{from}", from)
                        .replace("{to}", to)
        );
        ticket.setNote(config.getNote());

        Station departure = stationRepository.findByName(from);
        ticket.setRoute(departure.getRoute());

        return ticket;
    }

    @Override
    public boolean validateStations(String from, String to) {
        Station departure = stationRepository.findByName(from);
        Station arrival = stationRepository.findByName(to);
        return departure != null && arrival != null &&
                departure.getRoute().equals(arrival.getRoute());
    }

    @Override
    public Ticket createTicket(TicketType type, User user) {
        Ticket ticket = previewTicket(type);
        ticket.setUser(user);
        return ticket;
    }
    @Override
    public Page<Ticket> getTicketsByUser(User user, Pageable pageable) {
        return ticketRepository.findAllTicketsByUser(user, pageable);
    }
}
