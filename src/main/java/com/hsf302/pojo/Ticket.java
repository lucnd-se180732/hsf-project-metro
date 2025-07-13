package com.hsf302.pojo;

import com.hsf302.enums.TicketType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Ticket {
    @Id @GeneratedValue
    private Long id;

    private TicketType ticketType; // "single", "1day", "3day", "monthly"
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private boolean used; // dùng để check QR
    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String qrCode;
    private BigDecimal price;
    private LocalDateTime activatedAt;
    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @ManyToOne
    private User user;

    @ManyToOne
    private Route route;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String departureStation;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String arrivalStation;
    // Mô tả công dụng, thông tin mở rộng của vé
    @Column(columnDefinition = "NVARCHAR(255)")
    private String description;

    // Lưu ý quan trọng: giới hạn, thời gian hiệu lực, điều kiện sử dụng
    @Column(columnDefinition = "NVARCHAR(255)")
    private String note;

    public Ticket() {
    }

    public Ticket(Long id, TicketType ticketType, LocalDateTime startDate, LocalDateTime endDate, boolean used, String qrCode, BigDecimal price, LocalDateTime activatedAt, User user, Route route, String departureStation, String arrivalStation, String description, String note) {
        this.id = id;
        this.ticketType = ticketType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.used = used;
        this.qrCode = qrCode;
        this.price = price;
        this.activatedAt = activatedAt;
        this.user = user;
        this.route = route;
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
        this.description = description;
        this.note = note;

    }

    public String getStatus() {
        if (ticketType == TicketType.SINGLE) {
            // Vé lượt: chỉ được 1 lần dùng, hạn 30 ngày
            LocalDateTime expiredAt = startDate.plusDays(30);
            if (used) return "✅ Đã sử dụng";
            if (LocalDateTime.now().isAfter(expiredAt)) return "❌ Hết hạn (quá 30 ngày)";
            return "⏳ Chưa sử dụng (còn hạn)";
        }

        // Vé ngày / tháng
        if (activatedAt == null) {
            return "🕓 Chưa kích hoạt";
        }

        return LocalDateTime.now().isBefore(endDate)
                ? "✅ Còn hạn"
                : "❌ Hết hạn";
    }
    public void activate() {
        if (activatedAt == null) {
            this.activatedAt = LocalDateTime.now();
            this.startDate = activatedAt;

            switch (ticketType) {
                case ONE_DAY -> this.endDate = activatedAt.plusDays(1);
                case THREE_DAY -> this.endDate = activatedAt.plusDays(3);
                case MONTHLY -> this.endDate = activatedAt.plusDays(30);
            }
        }
    }



}

