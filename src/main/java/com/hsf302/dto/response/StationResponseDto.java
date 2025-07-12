package com.hsf302.dto.response;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StationResponseDto {
    private Long id;
    private String stationCode;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private Boolean isTerminal;
    private Boolean isInterchange;
    private String facilities;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

