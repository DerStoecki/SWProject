package de.othr.sw.WatchTHot.WatchTHotstarter.service.api;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticIdentifier;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticType;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;

import java.util.List;

public interface IStatisticService {
    String getStatistic(StatisticType type, String time, Room room, StatisticIdentifier identifier);

    void loadRoomData(List<MqttClientData> roomList);
}
