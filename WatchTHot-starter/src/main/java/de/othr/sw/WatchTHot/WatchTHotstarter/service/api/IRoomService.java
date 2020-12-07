package de.othr.sw.WatchTHot.WatchTHotstarter.service.api;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticType;

public interface IRoomService {
    String getStatistic(StatisticType type, String time);
}
