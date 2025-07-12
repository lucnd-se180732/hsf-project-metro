package com.hsf302.dto.response;


import lombok.Data;

import java.util.UUID;

@Data
public class StationBusRouteResponseDto {
    private Long id;
    private Long stationId;
    private Long busRouteId;
    private Integer walkingDistanceM;
    private Integer walkingTimeMinutes;
}

