package com.hsf302.service.impl;


import com.hsf302.dto.request.BusRouteRequestDto;
import com.hsf302.dto.response.BusRouteResponseDto;
import com.hsf302.pojo.BusRoute;
import com.hsf302.mapper.BusRouteMapper;
import com.hsf302.repository.BusRouteRepository;
import com.hsf302.service.interfaces.IBusRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusRouteServiceImpl implements IBusRouteService {

    private final BusRouteRepository busRouteRepository;
    private final BusRouteMapper busRouteMapper;

    @Override
    public BusRouteResponseDto create(BusRouteRequestDto dto) {
        BusRoute busRoute = busRouteMapper.toEntity(dto);
        return busRouteMapper.toDto(busRouteRepository.save(busRoute));
    }

    @Override
    public BusRouteResponseDto update(Long id, BusRouteRequestDto dto) {
        BusRoute busRoute = busRouteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bus route not found"));
        busRouteMapper.updateEntity(busRoute, dto);
        return busRouteMapper.toDto(busRouteRepository.save(busRoute));
    }

    @Override
    public void delete(Long id) {
        busRouteRepository.deleteById(id);
    }

    @Override
    public BusRouteResponseDto getById(Long id) {
        return busRouteMapper.toDto(
                busRouteRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Bus route not found"))
        );
    }

    @Override
    public List<BusRouteResponseDto> getAll() {
        return busRouteRepository.findAll()
                .stream()
                .map(busRouteMapper::toDto)
                .toList();
    }
}
