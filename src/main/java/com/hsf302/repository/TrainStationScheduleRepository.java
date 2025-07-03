package com.hsf302.repository;

import com.hsf302.pojo.TrainStationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainStationScheduleRepository extends JpaRepository<TrainStationSchedule, Long> {
    // Optional: custom query methods here
}
