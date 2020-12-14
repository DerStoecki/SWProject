package de.othr.sw.WatchTHot.WatchTHotstarter.service.api;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Payload;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticIdentifier;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticType;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;

import java.util.List;
import java.util.Map;

public interface IRoomService {
    /**
     * Gets the Statistic of a room.
     * @param type Statistic type -> HOUR/DAY/WEEK/MONTH/YEAR (Maybe quarter year and half year in future)
     * @param time timeStamp in ISO UTC time
     * @param room the room for the calculation
     * @return the Satistic as a String
     */

    String getStatistic(StatisticType type, String time, Room room, StatisticIdentifier identifier);

    /**
     * Read from Repo and load all the Rooms; In Visualizer --> Allow to set "Visualize Room" or something...idk
     * @param apartment the Apartment, usually set by user after login
     */
    void loadRooms(Apartment apartment);

    Map<Room, Map<MqttClientData, List<Payload>>> getLatestPayloads();
}
