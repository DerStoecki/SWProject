package de.othr.sw.WatchTHot.WatchTHotstarter.service.mqtt;

import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttClientService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttServerService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttService;
import org.springframework.stereotype.Service;

/**
 * This is to simulate a MqttServer and Clients.
 * Usually you would have a seperate MqttBroker and With this app you would connect to it.
 * Alternatively you could Connect an extra Service and let them communicate via wss.
 * If I didn't have to simulate this, i would've made the "clientService" the service who connects to MQTT Broker
 * and then subscribes to all topics who r needed by the Application.
 * Furthermore a user should be able to "add" topics to subscription list, which is in this case /
 * under these circumstances not possible
 * thats why i simulate by: these things
 * clientService will create Dummy data and send them to the serverService, the serverService will save the
 * Data in the Database and the other services (Visulizer service etc etc) will be able to load data.
 */

@Service
public class MqttService implements IMqttService {

    private final IMqttClientService clientService;
    private final IMqttServerService serverService;

    public MqttService(IMqttClientService clientService, IMqttServerService serverService){
        this.clientService = clientService;
        this.serverService = serverService;
    }

    /**
     * TODO in Server: Set Thermostate
     * TODO in client --> Randomize Temperatures by new TemperatureValue
     * @param newTemperatureValue the new Temperature
     * @return
     */
    @Override
    public boolean setTemperature(float newTemperatureValue) {
        return false;
    }

    /**
     * TODO MAKE SCHEDULED every 10-20 seconds
     */
    private void task(){
        this.serverService.publishTopicAndPayload(this.clientService.getDataSimulations());
    }
}
