package com.hsf302.repository;

import com.hsf302.enums.TicketType;
import com.hsf302.pojo.TicketConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketConfigRepository extends JpaRepository<TicketConfig, Long> {
    TicketConfig findByTicketType(TicketType type);
}