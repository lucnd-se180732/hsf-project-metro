package com.hsf302.controller;

import com.hsf302.dto.request.StationBusRouteRequestDto;
import com.hsf302.service.interfaces.IBusRouteService;
import com.hsf302.service.interfaces.IStationBusRouteService;
import com.hsf302.service.interfaces.IStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/station-bus-maps")
@RequiredArgsConstructor
public class StationBusRouteController {

    private final IStationBusRouteService stationBusRouteService;
    private final IStationService stationService;
    private final IBusRouteService busRouteService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("mappings", stationBusRouteService.getAll());
        return "stationbusroute/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("mapping", new StationBusRouteRequestDto());
        model.addAttribute("stations", stationService.getAll());
        model.addAttribute("busRoutes", busRouteService.getAll());
        return "stationbusroute/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("mapping") StationBusRouteRequestDto dto) {
        stationBusRouteService.create(dto);
        return "redirect:/station-bus-maps";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        stationBusRouteService.delete(id);
        return "redirect:/station-bus-maps";
    }
}

