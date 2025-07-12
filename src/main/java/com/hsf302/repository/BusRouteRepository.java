package com.hsf302.repository;

import com.hsf302.entity.BusRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BusRouteRepository extends JpaRepository<BusRoute, Long> {
    boolean existsByRouteNumber(String routeNumber);
}

