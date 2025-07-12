package com.hsf302.service.interfaces;



import com.hsf302.dto.request.StationRequestDto;
import com.hsf302.dto.response.StationResponseDto;

import java.util.List;
import java.util.UUID;

public interface IStationService {
    StationResponseDto create(StationRequestDto dto);
    StationResponseDto update(Long id, StationRequestDto dto);
    void delete(Long id);
    StationResponseDto getById(Long id);
    List<StationResponseDto> getAll();
}

