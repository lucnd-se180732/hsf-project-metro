package com.hsf302.service.interfaces;

import com.hsf302.dto.request.StationRequestDto;
import com.hsf302.dto.response.StationResponseDto;
import com.hsf302.pojo.Route;
import com.hsf302.pojo.Station;

import java.util.List;

public interface StationService {
    List<Station> findByRouteOrderByStationOrder(Route route);
    Station findByName(String name);

    StationResponseDto create(StationRequestDto dto);
    StationResponseDto update(Long id, StationRequestDto dto);
    void delete(Long id);
    Station getById(Long id);
    List<Station> getAll();
}
