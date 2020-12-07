package de.othr.sw.WatchTHot.WatchTHotstarter.service.api;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticType;

public interface IApartmentService {
    IRoomService getRoomService();
    String getMeterData();
}
