package com.hsf302.service.impl;

import com.hsf302.pojo.FareMatrix;
import com.hsf302.repository.FareMatrixRepository;
import com.hsf302.service.interfaces.FareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class FareServiceImpl implements FareService {

    @Autowired
    private  FareMatrixRepository fareRepository;

    @Override
    public int getFare(String from, String to) {
        Optional<FareMatrix> direct = fareRepository.findByFromStationAndToStation(from, to);
        if (direct.isPresent())
            return direct.get().getPrice();

        Optional<FareMatrix> reverse = fareRepository.findByFromStationAndToStation(to, from);
        if (reverse.isPresent())
            return reverse.get().getPrice();

        throw new IllegalArgumentException("Không tìm thấy giá vé cho cặp ga: " + from + " - " + to);
    }
}