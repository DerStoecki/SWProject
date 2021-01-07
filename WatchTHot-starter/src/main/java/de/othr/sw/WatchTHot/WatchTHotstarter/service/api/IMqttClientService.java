package de.othr.sw.WatchTHot.WatchTHotstarter.service.api;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientSimulation;

import java.util.List;

public interface IMqttClientService {

    boolean setTemperature(float newTemperatureValue);

    List<MqttClientSimulation> getDataSimulations();


}
