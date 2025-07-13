package com.hsf302.repository;

import com.hsf302.pojo.FareMatrix;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FareMatrixRepository extends JpaRepository<FareMatrix, Long> {
    Optional<FareMatrix> findByFromStationAndToStation(String fromStation, String toStation);
}
