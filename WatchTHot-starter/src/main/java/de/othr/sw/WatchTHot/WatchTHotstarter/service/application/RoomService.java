package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Payload;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Topic;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticIdentifier;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticType;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.ApartmentRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.MqttClientDataRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.RoomRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.TopicRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IRoomService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoomService implements IRoomService {


    private final RoomRepository roomRepository;

    private final MqttClientDataRepository mqttClientDataRepository;

    private final TopicRepository topicRepository;

    private Map<Room, List<MqttClientData>> roomClientDataMap = new ConcurrentHashMap<>();

    private List<Room> roomList;
    private List<Room> dummyRooms = new ArrayList<>();

    private Map<MqttClientData, List<Topic>> clientDatatopicMap = new HashMap<>();

    private final IStatisticService statisticService;

    @Autowired
    public RoomService(RoomRepository roomRepository, MqttClientDataRepository clientDataRepository,
                       TopicRepository topicRepository, IStatisticService statisticService) throws IOException {
        this.roomRepository = roomRepository;
        this.mqttClientDataRepository = clientDataRepository;
        this.topicRepository = topicRepository;
        this.statisticService = statisticService;
        createDummyRooms();
    }

    private void createDummyRooms() throws IOException {
        if(roomRepository.getRoomsByApartmentId(Long.parseLong("1")).size() >= 5){
            return;
        }
        this.initJson(new String(Files.readAllBytes(Paths.get("src/main/java/de/othr/sw/WatchTHot/WatchTHotstarter/service/application/initJson/room/childrenRoom.json"))));
        this.initJson(new String(Files.readAllBytes(Paths.get("src/main/java/de/othr/sw/WatchTHot/WatchTHotstarter/service/application/initJson/room/kitchen.json"))));
        this.initJson(new String(Files.readAllBytes(Paths.get("src/main/java/de/othr/sw/WatchTHot/WatchTHotstarter/service/application/initJson/room/livingRoom.json"))));
        this.initJson(new String(Files.readAllBytes(Paths.get("src/main/java/de/othr/sw/WatchTHot/WatchTHotstarter/service/application/initJson/room/parentsRoom.json"))));
        this.initJson(new String(Files.readAllBytes(Paths.get("src/main/java/de/othr/sw/WatchTHot/WatchTHotstarter/service/application/initJson/room/utilityRoom.json"))));
    }

    private void initJson(String config){
        JsonObject jsonConfigObject = new Gson().fromJson(config, JsonObject.class);
        Room newRoom = new Room(jsonConfigObject.get("name").getAsString(), jsonConfigObject.get("floor").getAsInt());
        roomRepository.save(newRoom);
        this.dummyRooms.add(newRoom);
    }


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

    @Override
    public void setDummyData(Apartment dummyApartment) {
        this.dummyRooms.forEach(room -> {room.setApartment(dummyApartment); roomRepository.save(room);});

    }

    @Override
    public List<Room> getRoomsByApartment(Apartment apartment) {
       return this.roomRepository.getRoomsByApartment(apartment);
    }

    @Override
    public MqttClientData getClientById(Long id) {
        return this.mqttClientDataRepository.getMqttClientDataById(id);
    }
}
