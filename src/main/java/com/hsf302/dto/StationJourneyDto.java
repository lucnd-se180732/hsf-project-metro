package com.hsf302.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StationJourneyDto {
    private String stationCode;
    private String name;
    private double latitude;
    private double longitude;
    private String address;
    private List<BusRouteSimpleDto> nearbyBusRoutes;
}

