package com.hsf302.dto.request;


import lombok.Data;

import java.util.UUID;

@Data
public class StationBusRouteRequestDto {
    private Long stationId;
    private Long busRouteId;
    private Integer walkingDistanceM;
    private Integer walkingTimeMinutes;
}

