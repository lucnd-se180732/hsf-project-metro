package com.hsf302.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BusRouteResponseDto {
    private Long id;
    private String routeNumber;
    private String name;
    private String description;
    private String operator;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

