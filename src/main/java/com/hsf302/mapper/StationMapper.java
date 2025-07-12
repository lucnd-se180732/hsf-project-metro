package com.hsf302.mapper;


import com.hsf302.dto.request.StationRequestDto;
import com.hsf302.dto.response.StationResponseDto;
import com.hsf302.entity.Station;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StationMapper {

    Station toEntity(StationRequestDto dto);

    StationResponseDto toDto(Station entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Station entity, StationRequestDto dto);
}

