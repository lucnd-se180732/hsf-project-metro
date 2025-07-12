package com.hsf302.service.impl;

import com.hsf302.dto.request.StationBusRouteRequestDto;
import com.hsf302.dto.response.StationBusRouteResponseDto;
import com.hsf302.entity.Station;
import com.hsf302.entity.BusRoute;
import com.hsf302.entity.StationBusRoute;
import com.hsf302.mapper.StationBusRouteMapper;
import com.hsf302.repository.StationBusRouteRepository;
import com.hsf302.repository.StationRepository;
import com.hsf302.repository.BusRouteRepository;
import com.hsf302.service.interfaces.IStationBusRouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StationBusRouteServiceImpl implements IStationBusRouteService {

    private final StationBusRouteRepository stationBusRouteRepository;
    private final StationRepository stationRepository;
    private final BusRouteRepository busRouteRepository;
    private final StationBusRouteMapper stationBusRouteMapper;

    @Override
    public StationBusRouteResponseDto create(StationBusRouteRequestDto dto) {
        Station station = stationRepository.findById(dto.getStationId())
                .orElseThrow(() -> new RuntimeException("Station not found"));
        BusRoute busRoute = busRouteRepository.findById(dto.getBusRouteId())
                .orElseThrow(() -> new RuntimeException("Bus route not found"));

        StationBusRoute entity = stationBusRouteMapper.toEntity(dto);
        entity.setStation(station);
        entity.setBusRoute(busRoute);

        return stationBusRouteMapper.toDto(stationBusRouteRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        stationBusRouteRepository.deleteById(id);
    }

    @Override
    public StationBusRouteResponseDto getById(Long id) {
        return stationBusRouteMapper.toDto(
                stationBusRouteRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Station-BusRoute not found"))
        );
    }

    @Override
    public List<StationBusRouteResponseDto> getAll() {
        return stationBusRouteRepository.findAll()
                .stream()
                .map(stationBusRouteMapper::toDto)
                .toList();
    }
}

