package com.hsf302.controller;

import com.hsf302.enums.TicketType;
import com.hsf302.pojo.Ticket;
import com.hsf302.pojo.User;
import com.hsf302.repository.TicketRepository;
import com.hsf302.service.interfaces.UserService;
import com.hsf302.Utility.QRUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private QRUtil qrUtil;

    @Autowired
    private UserService userService;


    // chia loai ve ra


    // ✅ Danh sách vé người dùng
    @GetMapping
    public String listUserTickets(
            Model model,
            OAuth2AuthenticationToken authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestParam(defaultValue = "active") String type // active, inactive, expired
    ) {
        String email = authentication.getPrincipal().getAttribute("email");
        User user = userService.findByEmail(email);

        List<Ticket> allTickets = ticketRepository.findByUser(user);
        List<Ticket> filtered = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();

        for (Ticket ticket : allTickets) {
            // Tự động sinh QR nếu thiếu
            if (ticket.getQrCode() == null || ticket.getQrCode().isEmpty()) {
                String qr = qrUtil.generate("TICKET#" + ticket.getId());
                ticket.setQrCode(qr);
                ticketRepository.save(ticket);
            }

            boolean isExpired = ticket.getEndDate() != null && now.isAfter(ticket.getEndDate());
            boolean isActivated = ticket.getTicketType() == TicketType.SINGLE || ticket.getActivatedAt() != null;
            boolean isUsedSingle = ticket.getTicketType() == TicketType.SINGLE && ticket.isUsed();

            switch (type) {
                case "inactive" -> {
                    if (!isExpired && !isActivated) filtered.add(ticket);
                }
                case "expired" -> {
                    if (isExpired || isUsedSingle) filtered.add(ticket);
                }
                default -> { // active
                    if (!isExpired && isActivated && !isUsedSingle) filtered.add(ticket);
                }
            }
        }

        // Sắp vé mới nhất lên đầu
        filtered.sort(Comparator.comparing(Ticket::getId).reversed());

        int totalItems = filtered.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        List<Ticket> pagedTickets = new ArrayList<>();
        if (totalItems > 0 && page < totalPages) {
            int start = page * size;
            int end = Math.min(start + size, totalItems);
            pagedTickets = filtered.subList(start, end);
        }

        model.addAttribute("tickets", pagedTickets);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("activeTab", type);

        return "ticket/ticket-list";
    }



    @PostMapping("/activate/{id}")
    public String activateTicket(@PathVariable Long id, OAuth2AuthenticationToken authentication) {
        // ✅ Lấy đúng email từ Google OAuth2
        String email = authentication.getPrincipal().getAttribute("email");
        User user = userService.findByEmail(email);

        Optional<Ticket> optionalTicket = ticketRepository.findById(id);

        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            if (ticket.getUser().getId().equals(user.getId()) && ticket.getActivatedAt() == null) {
                ticket.activate(); // bạn đã có hàm activate()
                ticketRepository.save(ticket);
            }
        }

        return "redirect:/tickets";
    }

    // ✅ Quét QR kiểm tra vé
    @GetMapping("/check-ticket")
    public String checkTicket(@RequestParam("code") String code, Model model) {
        if (!code.startsWith("TICKET#")) {
            model.addAttribute("status", "❌ Mã QR không hợp lệ.");
            return "ticket/check-result";
        }

        try {
            Long ticketId = Long.parseLong(code.substring(7));
            Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);

            if (optionalTicket.isEmpty()) {
                model.addAttribute("status", "❌ Không tìm thấy vé.");
            } else {
                Ticket ticket = optionalTicket.get();

                // Vé lượt đã dùng
                if (ticket.getTicketType() == TicketType.SINGLE && ticket.isUsed()) {
                    model.addAttribute("status", "⚠️ Vé lượt đã được sử dụng. Vui lòng mua vé mới!");
                }
                // Vé chưa kích hoạt (với vé tháng/ngày)
                else if (ticket.getTicketType() != TicketType.SINGLE && ticket.getActivatedAt() == null) {
                    model.addAttribute("status", "⚠️ Vé chưa được kích hoạt.");
                }
                // Vé hết hạn
                else if (ticket.getEndDate() != null && LocalDateTime.now().isAfter(ticket.getEndDate())) {
                    model.addAttribute("status", "❌ Vé đã hết hạn sử dụng.");
                }
                // Vé hợp lệ
                else {
                    model.addAttribute("status", "✅ Vé hợp lệ. Cho phép lên tàu!");
                    model.addAttribute("ticket", ticket);

                    if (ticket.getTicketType() == TicketType.SINGLE) {
                        ticket.setUsed(true);
                        ticketRepository.save(ticket);
                    }
                }
            }
        } catch (Exception e) {
            model.addAttribute("status", "❌ Lỗi xử lý mã QR.");
        }

        return "ticket/check-result";
    }


    @GetMapping("/check-ticket/camera")
    public String showCameraScanner() {
        return "ticket/scan";
    }
}
