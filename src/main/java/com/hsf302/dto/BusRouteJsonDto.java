// src/main/java/com/hsf302/dto/BusRouteJson.java
package com.hsf302.dto;

import lombok.Data;

@Data
public class BusRouteJsonDto {
    private String routeNumber;
    private String name;
    private String operatingHours;
    private String frequency;
    private String direction;
    private String directionForward;
    private String directionBackward;
}
