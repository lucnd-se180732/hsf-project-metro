package com.hsf302.mapper;


import com.hsf302.dto.request.StationBusRouteRequestDto;
import com.hsf302.dto.response.StationBusRouteResponseDto;
import com.hsf302.pojo.StationBusRoute;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StationBusRouteMapper {

    StationBusRoute toEntity(StationBusRouteRequestDto dto);

    StationBusRouteResponseDto toDto(StationBusRoute entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget StationBusRoute entity, StationBusRouteRequestDto dto);
}

