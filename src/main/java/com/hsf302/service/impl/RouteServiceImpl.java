package com.hsf302.service.impl;

import com.hsf302.pojo.Route;
import com.hsf302.repository.RouteRepository;
import com.hsf302.service.interfaces.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteRepository routeRepository;

    @Override
    public List<Route> findAll() {
        return routeRepository.findAll();
    }

    @Override
    public Route findByRouteCode(String code) {
        return routeRepository.findByRouteCode(code);
    }
}
