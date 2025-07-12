package com.hsf302.repository;



import com.hsf302.entity.Station;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StationRepository extends JpaRepository<Station, Long> {

    @EntityGraph(attributePaths = {"stationBusRoutes", "stationBusRoutes.busRoute"})
    List<Station> findAll();
}

