package com.hsf302.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Giao dịch này thuộc vé nào
    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    // Ai thanh toán
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal amount;

    private String paymentMethod; // VNPAY, MOMO, CASH,...

    private String transactionId; // Mã giao dịch do VNPAY trả về (vnp_TxnRef)

    private String responseCode;  // vnp_ResponseCode (00 là thành công)

    private String bankCode;

    private LocalDateTime paymentTime;

    private boolean success;
}
