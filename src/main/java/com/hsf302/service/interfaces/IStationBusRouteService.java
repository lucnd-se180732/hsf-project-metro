package com.hsf302.service.interfaces;


import com.hsf302.dto.request.StationBusRouteRequestDto;
import com.hsf302.dto.response.StationBusRouteResponseDto;

import java.util.List;
import java.util.UUID;

public interface IStationBusRouteService {
    StationBusRouteResponseDto create(StationBusRouteRequestDto dto);
    void delete(Long id);
    StationBusRouteResponseDto getById(Long id);
    List<StationBusRouteResponseDto> getAll();
}

