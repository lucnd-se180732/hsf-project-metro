package com.hsf302.mapper;

import com.hsf302.dto.request.BusRouteRequestDto;
import com.hsf302.dto.response.BusRouteResponseDto;
import com.hsf302.pojo.BusRoute;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BusRouteMapper {

    BusRoute toEntity(BusRouteRequestDto dto);

    BusRouteResponseDto toDto(BusRoute entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget BusRoute entity, BusRouteRequestDto dto);
}


