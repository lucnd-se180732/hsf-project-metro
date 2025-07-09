package com.hsf302.controller;

import com.hsf302.enums.TicketType;
import com.hsf302.pojo.*;
import com.hsf302.repository.StationRepository;
import com.hsf302.repository.TicketConfigRepository;
import com.hsf302.service.interfaces.FareService;
import com.hsf302.service.interfaces.TicketService;
import com.hsf302.service.interfaces.PaymentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
    @Autowired
    private  StationRepository stationRepository;
    @Autowired
    private TicketConfigRepository ticketConfigRepository;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private FareService fareService;

    @GetMapping
    public String chooseTicketType(Model model) {
        model.addAttribute("ticketTypes", TicketType.values());
        return "booking/index";
    }


    // Hiển thị chi tiết loại vé
    @GetMapping("/type/{type}")
    public String viewTicketDetail(@PathVariable("type") TicketType type, Model model) {
        Ticket ticket = ticketService.previewTicket(type);
        model.addAttribute("ticket", ticket);
        return "booking/type-detail";
    }


    @GetMapping("/single")
    public String chooseDepartureStation(Model model) {
        List<Station> stations = stationRepository.findAll();
        model.addAttribute("stations", stations);
        return "booking/single-from";
    }

    @GetMapping("/single/from/{stationId}")
    public String chooseArrivalStation(@PathVariable Long stationId, Model model) {
        Station fromStation = stationRepository.findById(stationId).orElse(null);
        if (fromStation == null) return "redirect:/booking/single";

        List<Station> destinations = fromStation.getRoute()
                .getStations()
                .stream()
                .filter(s -> !s.getId().equals(fromStation.getId()))
                .toList();

        model.addAttribute("from", fromStation);
        model.addAttribute("destinations", destinations);
        return "booking/single-to";
    }


    @GetMapping("/single/buy")
    public String viewSingleTicketDetail(@RequestParam String from, @RequestParam String to, Model model) {
        int fare = fareService.getFare(from, to);

        Ticket ticket = new Ticket();
        ticket.setTicketType(TicketType.SINGLE);
        ticket.setStartDate(LocalDateTime.now());
        ticket.setEndDate(LocalDateTime.now());
        ticket.setPrice(BigDecimal.valueOf(fare));
        ticket.setDepartureStation(from);
        ticket.setArrivalStation(to);
        ticket.setDescription("Vé lượt - cho phép đi 1 lần từ ga " + from + " đến ga " + to);
        ticket.setNote("Chỉ sử dụng được 1 lần và hết hiệu lực sau khi quét QR");

        model.addAttribute("ticket", ticket);
        return "booking/confirm-single";
    }


    @PostMapping("/confirm")
    public String confirmBooking(@ModelAttribute Ticket ticket, HttpSession session) {
        User user = (User) session.getAttribute("currentUser");
        if (user == null) return "redirect:/login";

        ticket.setUser(user);
        String url = paymentService.generatePaymentUrl(ticket);
        return "redirect:" + url;
    }

    //Thanh Toan Ve Luot
    @GetMapping("/booking/single/buy")
    public String viewSingleTicket(
            @RequestParam String from,
            @RequestParam String to,
            Model model) {

        Station departure = stationRepository.findByName(from);
        Station arrival = stationRepository.findByName(to);

        if (departure == null || arrival == null) {
            throw new RuntimeException("Không tìm thấy trạm.");
        }

        // Lấy route chung từ 1 trong 2 trạm (giả sử cùng tuyến)
        Route route = departure.getRoute();

        // 👉 Lấy giá vé đúng theo bảng fares
        int fare = fareService.getFare(from, to);  // ✅ CHỈNH ĐOẠN NÀY

        Ticket ticket = new Ticket();
        ticket.setTicketType(TicketType.SINGLE);
        ticket.setDepartureStation(from);
        ticket.setArrivalStation(to);
        ticket.setRoute(route);
        ticket.setPrice(BigDecimal.valueOf(fare)); // ✅ gán giá đúng
        ticket.setDescription("Vé lượt cho tuyến " + route.getName());
        ticket.setNote("Vé có hiệu lực 1 lần, sử dụng trong ngày.");

        model.addAttribute("ticket", ticket);

        return "booking/single-detail";
    }

    @GetMapping("/success")
    public String paymentSuccess() {
        return "booking/success"; // ✅ Tên view đúng theo thư mục
    }

    @GetMapping("/failed")
    public String paymentFailed() {
        return "booking/failed";  // ✅ View chính xác: templates/booking/failed.html
    }



}
