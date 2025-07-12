package com.hsf302.dto.request;


import lombok.Data;

@Data
public class StationRequestDto {
    private String stationCode;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean isTerminal;
    private Boolean isInterchange;
    private String facilities;
    private Boolean isActive;
}
