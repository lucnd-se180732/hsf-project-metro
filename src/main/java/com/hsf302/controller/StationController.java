package com.hsf302.controller;


import com.hsf302.dto.request.StationRequestDto;
import com.hsf302.service.interfaces.IStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/stations")
@RequiredArgsConstructor
public class StationController {

    private final IStationService stationService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("stations", stationService.getAll());
        return "station/list"; // Thymeleaf template
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("station", new StationRequestDto());
        return "station/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute("station") StationRequestDto dto, BindingResult result) {
        if (result.hasErrors()) return "station/create";
        stationService.create(dto);
        return "redirect:/stations";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("station", stationService.getById(id));
        return "station/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable Long id, @ModelAttribute("station") StationRequestDto dto) {
        stationService.update(id, dto);
        return "redirect:/stations";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        stationService.delete(id);
        return "redirect:/stations";
    }
}