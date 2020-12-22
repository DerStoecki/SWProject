package de.othr.sw.WatchTHot.WatchTHotstarter.service.mqtt;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientSimulation;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Topic;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.MqttClientDataRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.TopicRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MqttServerService implements IMqttServerService {

    private final TopicRepository topicRepository;

    private final MqttClientDataRepository dataRepository;
    private List<String> topics = new ArrayList<>();



    public MqttServerService(TopicRepository topicRepository, MqttClientDataRepository dataRepo){
        this.topicRepository = topicRepository;
        this.dataRepository = dataRepo;
    }

    @Override
    public void publishTopicAndPayload(List<MqttClientSimulation> dataSimulations) {
        addNewTopics(dataSimulations);
        createNewPayloads(dataSimulations);

    }

    /**
     * TODO CREATES AND PERSIST PAYLOADS
     * @param dataSimulations dataFromClientService
     *
     */
    private void createNewPayloads(List<MqttClientSimulation> dataSimulations) {
    }

    /**
     * CREATE AND ADD NEW TOPICS IF NECC.
     * @param dataSimulations
     */
    private void addNewTopics(List<MqttClientSimulation> dataSimulations) {
    }
}
