package de.othr.sw.WatchTHot.WatchTHotstarter.service.mqtt;

import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttClientService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttServerService;

/**
 * Handles the Server and "Clients" (Simulations)
 */
public class MqttService {

    private IMqttServerService serverService = new MqttServerService();
    private IMqttClientService clientService = new MqttClientService();

}
