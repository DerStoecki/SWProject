package de.othr.sw.WatchTHot.WatchTHotstarter.service.mqtt;

import com.google.gson.JsonObject;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientSimulation;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttClientService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MqttClientService implements IMqttClientService {

    private float setPointTemperature;


    /*
    * TODO SEND RANDOM DATA TO SERVER
    *  RECEIVE A "CHANGE TEMPERATURE" and lower temperature then or change by a amount or something...idk...
    * TODO Implement Method to randomize the Data of thermostat
    *   increase consumption of meter on higher temperatures
    * */

    private List<MqttClientSimulation> dataSimulations;
    private Map<String, JsonObject> objectsPayloads;

    public MqttClientService(){
        //TODO LOAD JSON
        //TODO CREATE DATA SIMULATION AND GET PAYLAODS
    }


    //TODO SET TEMPERATURE OF THERMOSTAT
    @Override
    public boolean setTemperature(float newTemperatureValue) {
        this.setPointTemperature = newTemperatureValue;
        changePayloadsTemperatureSensor();
        changeThermostatPayload(newTemperatureValue);
        return false;
    }

    //TODO get current thermostat and change payload etc etc
    private void changeThermostatPayload(float newTemperatureValue) {
    }

    //TODO MAKE TIMED!...every 20 seconds or so...
    private void changePayloadsTemperatureSensor() {
    }

    @Override
    public List<MqttClientSimulation> getDataSimulations() {
        return dataSimulations;
    }
}
