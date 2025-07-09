package com.hsf302.service.impl;

import com.hsf302.Utility.VNPayUtil;
import com.hsf302.config.VNPayConfig;
import com.hsf302.pojo.Payment;
import com.hsf302.pojo.Ticket;
import com.hsf302.pojo.User;
import com.hsf302.repository.PaymentRepository;
import com.hsf302.repository.TicketRepository;
import com.hsf302.service.interfaces.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private VNPayConfig config;

    @Override
        public String generatePaymentUrl(Ticket ticket) {
            String orderId = String.valueOf(System.currentTimeMillis());
            String amount = String.valueOf(ticket.getPrice().multiply(BigDecimal.valueOf(100)).longValue());

            Map<String, String> vnpParams = new TreeMap<>();
            vnpParams.put("vnp_Version", config.getVersion());
            vnpParams.put("vnp_Command", config.getCommand());
            vnpParams.put("vnp_TmnCode", config.getTmnCode());
            vnpParams.put("vnp_Amount", amount);
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_TxnRef", orderId);
            vnpParams.put("vnp_OrderInfo", "TICKET#" + ticket.getId());
            vnpParams.put("vnp_OrderType", "billpayment");
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_ReturnUrl", config.getReturnUrl());
            vnpParams.put("vnp_IpAddr", "127.0.0.1");
            vnpParams.put("vnp_CreateDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();

            for (Map.Entry<String, String> entry : vnpParams.entrySet()) {
                String encodedValue = URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII);
                hashData.append(entry.getKey()).append('=').append(encodedValue).append('&');
                query.append(entry.getKey()).append('=').append(encodedValue).append('&');
            }

            hashData.setLength(hashData.length() - 1);
            query.setLength(query.length() - 1);

            String secureHash = VNPayUtil.hmacSHA512(config.getHashSecret(), hashData.toString());
            query.append("&vnp_SecureHash=").append(secureHash);

            return config.getPayUrl() + "?" + query;
            // Ban goc ne
    }

    @Override
    public boolean verifyPayment(Map<String, String> vnpParams) {
        // ✅ Lấy secureHash từ VNPay và xoá khỏi map
        String secureHash = vnpParams.remove("vnp_SecureHash");

        // ✅ Dùng TreeMap để tự động sắp xếp key theo alphabet
        Map<String, String> sortedParams = new TreeMap<>(vnpParams);

        // ✅ Tạo chuỗi hash CÓ encode giá trị
        StringBuilder hashData = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            // Encode giá trị giống như khi tạo payment URL
            String encodedValue = URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII); // <-- Thêm dòng này
            hashData.append(entry.getKey()).append('=').append(encodedValue).append('&'); // <-- Sử dụng encodedValue
        }
        if (hashData.length() > 0) {
            hashData.setLength(hashData.length() - 1); // bỏ dấu & cuối
        }

        // ✅ Tính lại hash
        String checkHash = VNPayUtil.hmacSHA512(config.getHashSecret(), hashData.toString());
        String responseCode = vnpParams.get("vnp_ResponseCode");

        // 🔍 In DEBUG quan trọng
        System.out.println(">>> [DEBUG] VNPay trả về hash:      " + secureHash);
        System.out.println(">>> [DEBUG] Hash bạn tính lại:      " + checkHash);
        System.out.println(">>> [DEBUG] Mã phản hồi VNP:        " + responseCode);
        System.out.println(">>> [DEBUG] Chuỗi hashData:         " + hashData);
        System.out.println(">>> [DEBUG] So sánh kết quả:        " + (checkHash.equals(secureHash)));

        boolean success = checkHash.equals(secureHash) && "00".equals(responseCode);
        if (!success) {
            System.out.println("❌ Thanh toán thất bại - sai hash hoặc mã phản hồi khác 00");
            return false;
        }

        // ✅ Lưu thanh toán nếu đúng
        try {
            String orderInfo = vnpParams.get("vnp_OrderInfo");
            String transactionId = vnpParams.get("vnp_TxnRef");
            String bankCode = vnpParams.get("vnp_BankCode");
            BigDecimal amount = new BigDecimal(vnpParams.get("vnp_Amount")).divide(BigDecimal.valueOf(100));
            LocalDateTime paymentTime = LocalDateTime.parse(vnpParams.get("vnp_PayDate"), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

            Long ticketId = extractTicketIdFromOrderInfo(orderInfo);
            if (ticketId == null) {
                System.out.println("❌ Không thể lấy ticketId từ OrderInfo: " + orderInfo);
                return false;
            }

            Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
            if (ticket == null) {
                System.out.println("❌ Không tìm thấy Ticket ID: " + ticketId);
                return false;
            }

            Payment payment = new Payment();
            payment.setTicket(ticket);
            payment.setUser(ticket.getUser());
            payment.setAmount(amount);
            payment.setPaymentTime(paymentTime);
            payment.setBankCode(bankCode);
            payment.setTransactionId(transactionId);
            payment.setResponseCode(responseCode);
            payment.setPaymentMethod("VNPAY");
            payment.setSuccess(true);
            paymentRepository.save(payment);

            System.out.println("✅ Đã lưu thông tin thanh toán thành công.");
        } catch (Exception e) {
            System.out.println("❌ Lỗi khi xử lý thông tin thanh toán: " + e.getMessage());
            e.printStackTrace();
            return false;
        }

        return true;
        //Ban goc ne
    }


    private Long extractTicketIdFromOrderInfo(String orderInfo) {
        try {
            if (orderInfo != null && orderInfo.contains("TICKET#")) {
                return Long.parseLong(orderInfo.split("TICKET#")[1].trim());
            }
        } catch (Exception ignored) {}
        return null;
    }


    @Override
    public Page<Payment> getPaymentsByUser(User user, Pageable pageable) {
        return paymentRepository.findAllByUser(user, pageable);
    }
    @Override
    public void save(Payment payment) {
        paymentRepository.save(payment);
    }
    @Override
    public Optional<Payment> findByTicket(Ticket ticket) {
        return paymentRepository.findByTicket(ticket);
    }

}
