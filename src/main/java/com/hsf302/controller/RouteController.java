package com.hsf302.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsf302.dto.StationDTO;
import com.hsf302.pojo.Route;
import com.hsf302.pojo.Station;
import com.hsf302.service.interfaces.RouteService;
import com.hsf302.service.interfaces.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/routes")
public class RouteController {
    @Autowired
    private RouteService routeService;

    @Autowired
    private StationService stationService;

    // Danh sách tuyến metro
    @GetMapping
    public String listRoutes(Model model) {
        List<Route> routes = routeService.findAll();
        model.addAttribute("routes", routes);
        return "route/list";
    }

    @GetMapping("/{routeCode}")
    public String routeDetail(@PathVariable String routeCode, Model model) throws JsonProcessingException {
        Route route = routeService.findByRouteCode(routeCode);
        List<Station> stations = stationService.findByRouteOrderByStationOrder(route);

        List<StationDTO> stationDTOs = stations.stream().map(s -> {
            StationDTO dto = new StationDTO();
            dto.setName(s.getName());
            dto.setLatitude(s.getLatitude());
            dto.setLongitude(s.getLongitude());
            dto.setDistanceFromStartKm(s.getDistanceFromStartKm());
            dto.setStationOrder(s.getStationOrder());
            return dto;
        }).toList();

        ObjectMapper mapper = new ObjectMapper();
        String stationsJson = mapper.writeValueAsString(stationDTOs);

        model.addAttribute("route", route);
        model.addAttribute("stationsJson", stationsJson); // để dùng cho JS
        model.addAttribute("stations", stations);
        return "route/detail";
    }
}
