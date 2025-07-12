package com.hsf302.repository;

import com.hsf302.entity.StationBusRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StationBusRouteRepository extends JpaRepository<StationBusRoute, Long> {
    boolean existsByStation_IdAndBusRoute_Id(Long stationId, Long busRouteId);
    List<StationBusRoute> findByStationId(Long stationId);
}

