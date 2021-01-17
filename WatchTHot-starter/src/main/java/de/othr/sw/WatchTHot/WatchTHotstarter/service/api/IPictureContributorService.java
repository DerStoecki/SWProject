package de.othr.sw.WatchTHot.WatchTHotstarter.service.api;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.Statistic;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticIdentifier;

import java.util.List;

public interface IPictureContributorService {

    boolean sendStatistic(StatisticIdentifier identifier, List<Statistic> statisticList);

}
