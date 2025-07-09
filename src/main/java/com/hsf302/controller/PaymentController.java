package com.hsf302.controller;

import com.hsf302.enums.TicketType;
import com.hsf302.pojo.*;
import com.hsf302.repository.StationRepository;
import com.hsf302.repository.TicketRepository;
import com.hsf302.service.interfaces.PaymentService;
import com.hsf302.Utility.QRUtil;
import com.hsf302.service.interfaces.UserService;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Controller

@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private  PaymentService vnPayService;
    @Autowired
    private  StationRepository stationRepository;
    @Autowired
    private  TicketRepository ticketRepository;
    @Autowired
    private  EntityManager entityManager;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UserService userService;


    @PostMapping("/vnpay")
    public String initiatePayment(@RequestParam(required = false) String from,
                                  @RequestParam(required = false) String to,
                                  @RequestParam TicketType ticketType,
                                  @RequestParam BigDecimal price,
                                  @RequestParam String startDate,
                                  @RequestParam String endDate,
                                  @RequestParam String description,
                                  @RequestParam String note,
                                  HttpSession session) {

        Ticket ticket = new Ticket();
        ticket.setTicketType(ticketType);
        ticket.setPrice(price);
        ticket.setDescription(description);
        ticket.setNote(note);
        ticket.setUsed(false);

        // ✅ Phân biệt rõ xử lý giữa vé SINGLE và các loại vé khác
        if (ticketType == TicketType.SINGLE) {
            // 🎯 Vé lượt: có hạn dùng 30 ngày từ lúc mua, chỉ dùng được 1 lần
            ticket.setStartDate(java.time.LocalDateTime.now());
            ticket.setEndDate(java.time.LocalDateTime.now().plusDays(30));

            Station departure = stationRepository.findByName(from);
            Station arrival = stationRepository.findByName(to);
            Route route = departure.getRoute();

            ticket.setDepartureStation(from);
            ticket.setArrivalStation(to);
            ticket.setRoute(route);
        } else {
            // 🎯 Vé ngày/tháng: chưa kích hoạt lúc mua => startDate & endDate null
            ticket.setStartDate(null);
            ticket.setEndDate(null);
        }

        // ✅ Lấy user từ session và chuyển thành entity proxy
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser != null) {
            User managedUser = entityManager.getReference(User.class, sessionUser.getId());
            ticket.setUser(managedUser);
        } else {
            throw new RuntimeException("⚠ Không tìm thấy user trong session");
        }

        // ✅ Lưu vào DB trước để có ticketId
        Ticket savedTicket = ticketRepository.save(ticket);

        // 💾 (Tuỳ chọn) giữ vé trong session nếu cần dùng lại sau
        session.setAttribute("pendingTicket", savedTicket);

        // ✅ Sinh URL với ticket đã có ID
        String paymentUrl = vnPayService.generatePaymentUrl(savedTicket);
        return "redirect:" + paymentUrl;
    }


    @GetMapping("/vnpay-return")
    public String vnpayReturn(@RequestParam Map<String, String> params, HttpSession session) {
        boolean isSuccess = vnPayService.verifyPayment(params);

        if (isSuccess) {
            Ticket ticket = (Ticket) session.getAttribute("pendingTicket");

            // Nếu không có ticket hoặc ticket null thì redirect lỗi
            if (ticket == null || ticket.getId() == null) {
                return "redirect:/booking/failed";
            }

            // ✅ Check lại từ DB để chắc chắn
            Ticket freshTicket = ticketRepository.findById(ticket.getId()).orElse(null);
            if (freshTicket == null) {
                return "redirect:/booking/failed";
            }

            // Nếu vé đã từng được thanh toán, thì không làm lại nữa
            Optional<Payment> existing = paymentService.findByTicket(freshTicket);
            if (existing.isPresent()) {
                return "redirect:/booking/success"; // hoặc failed nếu bạn muốn ngăn
            }

            // ✅ Gán QR & save
            String qrContent = "TICKET#" + freshTicket.getId();
            String qrCodeBase64 = QRUtil.generate(qrContent);
            freshTicket.setQrCode(qrCodeBase64);
            ticketRepository.save(freshTicket);

            return "redirect:/booking/success";
        } else {
            return "redirect:/booking/failed";
        }
    }


    @GetMapping("/history")
    public String viewHistory(
            Model model,
            OAuth2AuthenticationToken authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        String email = authentication.getPrincipal().getAttribute("email");
        User user = userService.findByEmail(email);

        Page<Payment> paymentPage = paymentService.getPaymentsByUser(user, PageRequest.of(page, size));

        model.addAttribute("payments", paymentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paymentPage.getTotalPages());

        return "payment/history"; // file payment/history.html
    }


}
