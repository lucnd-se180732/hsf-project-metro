package com.hsf302.service.interfaces;
import com.hsf302.dto.request.BusRouteRequestDto;
import com.hsf302.dto.response.BusRouteResponseDto;

import java.util.List;
import java.util.UUID;

public interface IBusRouteService {
    BusRouteResponseDto create(BusRouteRequestDto dto);
    BusRouteResponseDto update(Long id, BusRouteRequestDto dto);
    void delete(Long id);
    BusRouteResponseDto getById(Long id);
    List<BusRouteResponseDto> getAll();
}
