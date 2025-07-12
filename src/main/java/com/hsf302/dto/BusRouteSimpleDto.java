package com.hsf302.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusRouteSimpleDto {
    private String routeNumber;
    private String name;
    private int walkingDistanceM;
    private int walkingTimeMinutes;
    private String description;
    private String frequency;
    private String direction;
    private String directionForward;
    private String directionBackward;
}

