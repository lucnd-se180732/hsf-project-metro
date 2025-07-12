package com.hsf302.controller;

import com.hsf302.dto.StationJourneyDto;
import com.hsf302.service.interfaces.IJourneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/journey")
@RequiredArgsConstructor
public class JourneyController {

    private final IJourneyService journeyService;

    // View hành trình với danh sách ga và tuyến buýt gần mỗi ga
    @GetMapping
    public String viewJourneyPage(Model model) {
        List<StationJourneyDto> stations = journeyService.getAllStationsWithNearbyBusRoutes();
        model.addAttribute("stations", stations);
        return "StationBusRoute/journey"; // tên file HTML trong templates/customer/journey.html
    }
}