package com.hsf302.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationDTO {
    private String name;
    private double latitude;
    private double longitude;
    private double distanceFromStartKm;
    private int stationOrder;
}
