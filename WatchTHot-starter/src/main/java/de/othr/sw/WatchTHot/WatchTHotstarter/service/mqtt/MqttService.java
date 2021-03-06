package de.othr.sw.WatchTHot.WatchTHotstarter.service.mqtt;

import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttClientService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttServerService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * This is to simulate a MqttServer and Clients.
 * Usually you would have a seperate MqttBroker and With this app you would connect to it.
 * Alternatively you could Connect an extra Service and let them communicate via wss.
 * If I didn't have to simulate this, i would've made the "clientService" the service who connects to MQTT Broker
 * and then subscribes to all topics who r needed by the Application.
 * Furthermore a user should be able to "add" topics to subscription list, which is in this case /
 * under these circumstances not possible
 * that's why i simulate by: these things
 * clientService will create Dummy data and send them to the serverService, the serverService will save the
 * Data in the Database and the other services (Visualizer service etc etc) will be able to load data.
 *
 * MqttClient can be found in my GithubRepo here:
 * https://github.com/DerStoecki/openems/tree/feature/MQTT/io.openems.edge.bridge.mqtt/src/io/openems/edge/bridge/mqtt
 *
 */

@Service
public class MqttService implements IMqttService {

    private final IMqttClientService clientService;
    private final IMqttServerService serverService;

    public MqttService(IMqttClientService clientService, IMqttServerService serverService){
        this.clientService = clientService;
        this.serverService = serverService;
        this.setTemperature(21.2f);
        this.task();
    }

    /**
     * @param newTemperatureValue the new Temperature
     * @return true on success
     */
    @Override
    public boolean setTemperature(float newTemperatureValue) {
        if( this.clientService.setTemperature(newTemperatureValue)){
            this.task();
            return true;
        }
        return false;
    }


    @Scheduled(cron = "*/20 * * * * *")
    private void task(){
        this.serverService.publishTopicAndPayload(this.clientService.getDataSimulations());
    }
}
