package de.othr.sw.WatchTHot.WatchTHotstarter.service.api;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.Statistic;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticIdentifier;

import java.util.List;

public interface IStatisticService {
    Statistic getStatistic(String time, MqttClientData clientData, StatisticIdentifier identifier);

    void loadRoomData(List<MqttClientData> roomList);

    void calculateInitStatistic(MqttClientData data);
}
