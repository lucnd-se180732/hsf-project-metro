package com.hsf302.service.interfaces;

import com.hsf302.pojo.Route;

import java.util.List;

public interface RouteService {
    List<Route> findAll();
    Route findByRouteCode(String code);
}
