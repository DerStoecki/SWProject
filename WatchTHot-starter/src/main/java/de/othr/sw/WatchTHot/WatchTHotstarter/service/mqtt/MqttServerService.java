package de.othr.sw.WatchTHot.WatchTHotstarter.service.mqtt;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientSimulation;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Payload;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Topic;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.*;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttServerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MqttServerService implements IMqttServerService {

    private final TopicRepository topicRepository;

    private final MqttClientDataRepository dataRepository;
    private final PayloadRepository payloadRepository;
    private final RoomRepository roomRepository;
    private final ApartmentRepository apartmentRepository;
    private Map<String, Topic> topicNameAndTopic = new HashMap<>();



    public MqttServerService(TopicRepository topicRepository, MqttClientDataRepository dataRepo,
                             PayloadRepository payloadRepository, RoomRepository roomRepository,
                             ApartmentRepository apartmentRepository){
        this.topicRepository = topicRepository;
        this.dataRepository = dataRepo;
        this.payloadRepository = payloadRepository;
        this.roomRepository = roomRepository;
        this.apartmentRepository = apartmentRepository;
    }

    /**
     * Usually Called by MqttService;
     * Create a new Topic if exists and add Payloads
     * @param dataSimulations the simulated Data.
     */
    @Override
    @Transactional
    public void publishTopicAndPayload(List<MqttClientSimulation> dataSimulations) {
        addNewTopics(dataSimulations);
        createNewPayloads(dataSimulations);
        createNewMqttClient(dataSimulations);

    }

    private void createNewMqttClient(List<MqttClientSimulation> dataSimulations) {
        dataSimulations.forEach(data->{
            boolean changesWereMade = false;
            Optional<Room> room = Optional.ofNullable(this.roomRepository.getRoomByRoomNameAndApartment(data.getRoom(), apartmentRepository.getApartmentById(data.getApartmentId())));
            Optional<MqttClientData> mqttClientData = Optional.ofNullable(this.dataRepository.
                    getMqttClientDataByName(data.getName()));
            MqttClientData client;
            //Thank you IntelliJ
            client = mqttClientData.orElseGet(() -> new MqttClientData(data.getName(), data.getDeviceType()));
            if(mqttClientData.isEmpty()){
                dataRepository.save(client);
            }
                if(client.getRoom() == null && room.isPresent()){
                    //SET room for client and update room;
                    client.setRoom(room.get());
                    room.get().addData(client);
                    changesWereMade = true;

                }
                //client add Topic of simulated Data
                if(client.addTopic(this.topicNameAndTopic.get(data.getTopic()))){
                    changesWereMade = true;
                }
            if(changesWereMade){
                this.dataRepository.save(client);
                Topic topic = topicRepository.getTopicByTopic(data.getTopic());
                topic.setMqttClientData(client);
            }
        });


    }

    /**
     * Creates a new Payload, adds it to Topic and then saves both of them. Called by publishTopicAndPayload
     * @param dataSimulations dataFromClientService
     *
     */
    public void createNewPayloads(List<MqttClientSimulation> dataSimulations) {
        dataSimulations.forEach(dataSimulation->{
                Topic topic = this.topicNameAndTopic.get(dataSimulation.getTopic());
                String key = "";
                if(dataSimulation.getPayload().has("temperature")){
                    key = "temperature";
                } else if(dataSimulation.getPayload().has("meterState")){
                    key = "meterState";
                }
                if(!key.equals("")){
                    Payload payload = new Payload(topic, dataSimulation.getPayload().get(key).getAsString(), dataSimulation.getTime());
                    payloadRepository.save(payload);
                    topic.addPayload(payload);

                }
            });
    }

    /**
     * CREATE AND ADD NEW TOPICS IF NECESSARY.
     * @param dataSimulations dataSimulations from Simulator
     */
    private void addNewTopics(List<MqttClientSimulation> dataSimulations) {
        List<String> topics = new ArrayList<>(this.topicNameAndTopic.keySet());
        dataSimulations.forEach( dataSimulation -> {
            if(topics.stream().noneMatch(topic->topic.equals(dataSimulation.getTopic()))) {
                Optional<Topic> existingTopic = Optional.ofNullable(this.topicRepository.getTopicByTopic(dataSimulation.getTopic()));
                if (existingTopic.isEmpty()) {
                    Topic newTopic = new Topic(dataSimulation.getTopic());
                    this.topicRepository.save(newTopic);
                    this.topicNameAndTopic.put(dataSimulation.getTopic(), newTopic);
                } else {
                    this.topicNameAndTopic.put(dataSimulation.getTopic(), existingTopic.get());
                }
            }
        });
    }
}
