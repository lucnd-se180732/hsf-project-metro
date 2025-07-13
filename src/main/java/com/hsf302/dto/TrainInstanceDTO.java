package com.hsf302.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TrainInstanceDTO {
    private String trainCode;
    private String routeName;
    private String direction;
    private String currentStation;
    private LocalDateTime arrivalTime;
}
