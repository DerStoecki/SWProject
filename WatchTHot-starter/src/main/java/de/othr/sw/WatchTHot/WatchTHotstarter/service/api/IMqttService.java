package de.othr.sw.WatchTHot.WatchTHotstarter.service.api;

public interface IMqttService {
    /**
     * Set the Temperature of current Apartment; usually called by visualizer Service.
     * @param newTemperatureValue the new Temperature
     * @return true on success
     */
    boolean setTemperature(float newTemperatureValue);
}
