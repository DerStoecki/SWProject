package de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt;


import com.google.gson.JsonObject;

public class MqttClientSimulation {
    private String topic;
    private int qos;
    private JsonObject payload;
    private boolean retainFlag;
    private boolean cleanSession;
    private DeviceType deviceType;
    private int consumption;

    public MqttClientSimulation(String topic, JsonObject payload, DeviceType type, int consumption){
        this.topic = topic;
        this.payload = payload;
        this.deviceType = type;
        this.consumption = consumption;
    }

    public String getTopic() {
        return topic;
    }

    public int getQos() {
        return qos;
    }

    public JsonObject getPayload() {
        return payload;
    }

    public boolean isRetainFlag() {
        return retainFlag;
    }

    public boolean isCleanSession() {
        return cleanSession;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public int getConsumption() {
        return consumption;
    }
}
