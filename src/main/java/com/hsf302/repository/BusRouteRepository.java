package com.hsf302.repository;

import com.hsf302.pojo.BusRoute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRouteRepository extends JpaRepository<BusRoute, Long> {
    boolean existsByRouteNumber(String routeNumber);
}

