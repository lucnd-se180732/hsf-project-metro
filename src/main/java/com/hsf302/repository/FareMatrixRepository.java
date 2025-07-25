package com.hsf302.repository;

import com.hsf302.pojo.FareMatrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FareMatrixRepository extends JpaRepository<FareMatrix, Long> {
    @Query("SELECT f FROM FareMatrix f WHERE LOWER(f.fromStation) = LOWER(:fromStation) AND LOWER(f.toStation) = LOWER(:toStation)")
    Optional<FareMatrix> findFareIgnoreCase(@Param("fromStation") String fromStation, @Param("toStation") String toStation);
}
