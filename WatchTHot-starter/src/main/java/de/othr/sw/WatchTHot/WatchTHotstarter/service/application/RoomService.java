package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Payload;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Topic;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticIdentifier;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticType;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.MqttClientDataRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.RoomRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.TopicRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IRoomService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RoomService implements IRoomService {

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    MqttClientDataRepository mqttClientDataRepository;
    @Autowired
    TopicRepository topicRepository;

    private Map<Room, List<MqttClientData>> roomClientDataMap = new ConcurrentHashMap<>();

    private List<Room> roomList;

    private Map<MqttClientData, List<Topic>> clientDatatopicMap = new ConcurrentHashMap<>();

    private IStatisticService statisticService = new StatisticService();

    @Override
    public String getStatistic(StatisticType type, String time, Room room, StatisticIdentifier identifier) {
        return this.statisticService.getStatistic(type, time, room, identifier);
    }

    @Override
    public void loadRooms(Apartment apartment) {
        this.roomList = apartment.getRooms();
        this.roomList.forEach(room -> {
            this.roomClientDataMap.put(room,room.getData());
            room.getData().forEach(clientData -> this.clientDatatopicMap.put(clientData, clientData.getTopics()));
        });
        this.statisticService.loadRooms(this.roomList);
    }

    @Override
    public Map<Room, Map<MqttClientData, List<Payload>>> getLatestPayloads(){
        Map<MqttClientData,List<Payload>> clientDataPayloadMap = new ConcurrentHashMap<>();
        Map<Room, Map<MqttClientData, List<Payload>>> roomDataMap = new ConcurrentHashMap<>();
        List<Payload> mostRecentPayloads = new ArrayList<>();
        //Each room
        this.roomClientDataMap.forEach((room,clientData) ->{
            //Each Client Entry
            clientData.forEach(client -> {
                //Each Topic
                this.clientDatatopicMap.get(client).forEach(topic -> {
                    //List of Most Recent Payload
                    mostRecentPayloads.add(topic.getMostRecentPayload());
                });
                clientDataPayloadMap.put(client, mostRecentPayloads);
                mostRecentPayloads.clear();
            });
            roomDataMap.put(room, clientDataPayloadMap);
            clientDataPayloadMap.clear();
        });
        //List of Payloads due to Topics...
        return roomDataMap;
    }
}
