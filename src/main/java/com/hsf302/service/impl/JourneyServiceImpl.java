package com.hsf302.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.hsf302.dto.BusRouteSimpleDto;
import com.hsf302.dto.StationJourneyDto;
import com.hsf302.repository.StationRepository;
import com.hsf302.service.interfaces.IJourneyService;

import com.hsf302.dto.BusRouteJsonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JourneyServiceImpl implements IJourneyService {

    private final StationRepository stationRepository;





    @Override
    public List<StationJourneyDto> getAllStationsWithNearbyBusRoutes() {
        List<BusRouteJsonDto> extraBusRoutes = loadBusRouteJson();
        Map<String, BusRouteJsonDto> extraMap = extraBusRoutes.stream()
                .collect(Collectors.toMap(BusRouteJsonDto::getRouteNumber, b -> b));

        return stationRepository.findAll().stream().map(station -> {
            List<BusRouteSimpleDto> busRoutes = station.getStationBusRoutes().stream()
                    .map(link -> {
                        var busRoute = link.getBusRoute();
                        var extra = extraMap.get(busRoute.getRouteNumber());

                        return BusRouteSimpleDto.builder()
                                .routeNumber(busRoute.getRouteNumber())
                                .name(busRoute.getName())
                                .description(busRoute.getDescription())
                                .walkingDistanceM(link.getWalkingDistanceM())
                                .walkingTimeMinutes(link.getWalkingTimeMinutes())
                                .frequency(extra != null ? extra.getFrequency() : null)
                                .direction(extra != null ? extra.getDirection() : null)
                                .directionForward(extra != null ? extra.getDirectionForward() : null)
                                .directionBackward(extra != null ? extra.getDirectionBackward() : null)
                                .build();
                    }).collect(Collectors.toList());

            return StationJourneyDto.builder()
                    .stationCode(station.getStationCode())
                    .name(station.getName())
                    .address(station.getAddress())
                    .latitude(station.getLatitude())
                    .longitude(station.getLongitude())
                    .nearbyBusRoutes(busRoutes)
                    .build();
        }).collect(Collectors.toList());
    }

    private List<BusRouteJsonDto> loadBusRouteJson() {
        try (InputStream is = getClass().getResourceAsStream("/static/json/bus_route.json")) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(is, new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Không thể đọc file JSON bus-route.json", e);
        }
    }
}
