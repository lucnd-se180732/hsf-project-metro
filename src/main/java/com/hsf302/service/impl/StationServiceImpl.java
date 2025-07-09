package com.hsf302.service.impl;

import com.hsf302.pojo.Route;
import com.hsf302.pojo.Station;
import com.hsf302.repository.StationRepository;
import com.hsf302.service.interfaces.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationServiceImpl implements StationService {
    @Autowired
    private StationRepository stationRepository;

    @Override
    public List<Station> findByRouteOrderByStationOrder(Route route) {
        return stationRepository.findByRouteOrderByStationOrderAsc(route);
    }

    @Override
    public Station findByName(String name) {
        return stationRepository.findByName(name);
    }
}
