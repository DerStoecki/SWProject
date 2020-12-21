package de.othr.sw.WatchTHot.WatchTHotstarter.service.mqtt;

import de.othr.sw.WatchTHot.WatchTHotstarter.repository.TopicRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttServerService;
import org.springframework.beans.factory.annotation.Autowired;



public class MqttServerService implements IMqttServerService {
    @Autowired
    private TopicRepository topicRepository;
    /*
    * TODO SETUP SERVER AND SAVE IN REPOSITORY MQTTCLIENTDATA;TOPICS and PAYLOADS
    * */
}
