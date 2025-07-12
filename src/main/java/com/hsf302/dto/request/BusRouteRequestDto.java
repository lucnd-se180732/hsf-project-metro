package com.hsf302.dto.request;


import lombok.Data;

@Data
public class BusRouteRequestDto {
    private String routeNumber;
    private String name;
    private String description;
    private String operator;
    private Boolean isActive;
}

