package de.othr.sw.WatchTHot.WatchTHotstarter.repository;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.Statistic;
import org.springframework.data.repository.CrudRepository;

public interface StatisticRepository extends CrudRepository<Statistic, Long> {
    Statistic getStatisticById(Long id);
}
