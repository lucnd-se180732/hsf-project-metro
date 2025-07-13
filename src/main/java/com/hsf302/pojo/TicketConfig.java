package com.hsf302.pojo;

import com.hsf302.enums.TicketType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "ticket_config")
public class TicketConfig {
    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    private BigDecimal price;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String description;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String note;
}