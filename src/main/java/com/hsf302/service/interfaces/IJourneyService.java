package com.hsf302.service.interfaces;


import com.hsf302.dto.StationJourneyDto;
import java.util.List;

public interface IJourneyService {
    List<StationJourneyDto> getAllStationsWithNearbyBusRoutes();
}
