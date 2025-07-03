package com.hsf302.service.interfaces;

import com.hsf302.pojo.Route;
import com.hsf302.pojo.Station;

import java.util.List;

public interface StationService {
    List<Station> findByRouteOrderByStationOrder(Route route);
}
