package com.hsf302.repository;

import com.hsf302.pojo.Route;
import com.hsf302.pojo.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    List<Station> findByRouteOrderByStationOrderAsc(Route route);
}
