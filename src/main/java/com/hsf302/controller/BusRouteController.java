package com.hsf302.controller;

import com.hsf302.dto.request.BusRouteRequestDto;
import com.hsf302.service.interfaces.IBusRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/bus-routes")
@RequiredArgsConstructor
public class BusRouteController {

    private final IBusRouteService busRouteService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("routes", busRouteService.getAll());
        return "busroute/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("route", new BusRouteRequestDto());
        return "busroute/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("route") BusRouteRequestDto dto) {
        busRouteService.create(dto);
        return "redirect:/bus-routes";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("route", busRouteService.getById(id));
        return "busroute/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute("route") BusRouteRequestDto dto) {
        busRouteService.update(id, dto);
        return "redirect:/bus-routes";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        busRouteService.delete(id);
        return "redirect:/bus-routes";
    }
}

