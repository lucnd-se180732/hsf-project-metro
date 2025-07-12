package com.hsf302.service.impl;


import com.hsf302.dto.request.StationRequestDto;
import com.hsf302.dto.response.StationResponseDto;
import com.hsf302.entity.Station;
import com.hsf302.mapper.StationMapper;
import com.hsf302.repository.StationRepository;
import com.hsf302.service.interfaces.IStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements IStationService {

    private final StationRepository stationRepository;
    private final StationMapper stationMapper;

    @Override
    public StationResponseDto create(StationRequestDto dto) {
        Station station = stationMapper.toEntity(dto);
        return stationMapper.toDto(stationRepository.save(station));
    }



    @Override
    public StationResponseDto update(Long id, StationRequestDto dto) {
        Station station = stationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Station not found"));
        stationMapper.updateEntity(station, dto);
        return stationMapper.toDto(stationRepository.save(station));
    }

    @Override
    public void delete(Long id) {
        stationRepository.deleteById(id);
    }

    @Override
    public StationResponseDto getById(Long id) {
        return stationMapper.toDto(
                stationRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Station not found"))
        );
    }

    @Override
    public List<StationResponseDto> getAll() {
        return stationRepository.findAll()
                .stream()
                .map(stationMapper::toDto)
                .toList();
    }
}

