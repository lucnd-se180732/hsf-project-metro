package com.hsf302.controller;

import com.hsf302.dto.TrainInstanceDTO;
import com.hsf302.pojo.Train;
import com.hsf302.pojo.TrainStationSchedule;
import com.hsf302.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class TrainController {

    @Autowired
    private TrainRepository trainRepository;

    @GetMapping("/train/list")
    public String listTrains(Model model) {
        List<Train> trains = trainRepository.findAll();
        List<TrainInstanceDTO> result = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (Train train : trains) {
            List<TrainStationSchedule> schedules = train.getSchedules()
                    .stream()
                    .sorted(Comparator.comparing(TrainStationSchedule::getArrivalTime))
                    .toList();

            TrainStationSchedule current = null;

            for (TrainStationSchedule s : schedules) {
                if (!s.getArrivalTime().isAfter(now)) {
                    current = s;
                } else {
                    break;
                }
            }

            if (current == null) continue;

            String direction = schedules.get(0).getStation().getName() + " → " +
                    schedules.get(schedules.size() - 1).getStation().getName();

            TrainInstanceDTO dto = new TrainInstanceDTO(
                    train.getTrainCode(),
                    train.getRoute().getName(),
                    direction,
                    current.getStation().getName(),
                    current.getArrivalTime()
            );

            result.add(dto);
        }

        model.addAttribute("trains", result);
        return "train/list"; // view nằm tại templates/train/list.html
    }

}
