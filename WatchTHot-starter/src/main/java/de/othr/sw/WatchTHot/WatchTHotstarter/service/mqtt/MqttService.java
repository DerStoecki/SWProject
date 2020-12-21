package de.othr.sw.WatchTHot.WatchTHotstarter.service.mqtt;

import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttClientService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttServerService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class MqttService {
    IMqttClientService clientService = new MqttClientService();
    IMqttServerService serverService = new MqttServerService();
}
