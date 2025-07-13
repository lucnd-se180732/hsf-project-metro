package com.hsf302.repository;

import com.hsf302.pojo.StationBusRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationBusRouteRepository extends JpaRepository<StationBusRoute, Long> {
    boolean existsByStation_IdAndBusRoute_Id(Long stationId, Long busRouteId);
    List<StationBusRoute> findByStationId(Long stationId);
}

