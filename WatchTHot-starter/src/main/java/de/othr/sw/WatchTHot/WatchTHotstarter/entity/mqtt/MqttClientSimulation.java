package de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt;


import com.google.gson.JsonObject;
import org.joda.time.DateTime;

public class MqttClientSimulation {
    private String topic;
    private String name;
    private int qos;
    private JsonObject payload;
    private boolean retainFlag;
    private boolean cleanSession;
    private DeviceType deviceType;
    private int consumption;
    private String room;
    private DateTime time;

    public MqttClientSimulation(String topic, JsonObject payload, DeviceType type, String room, String name){
        this.topic = topic;
        this.payload = payload;
        this.deviceType = type;
        this.room = room;
        this.name = name;

    }
    public void setConsumption(int consumption){
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

    public String getRoom() {
        return room;
    }
    public void setTimeNow(){
        this.time = DateTime.now();
    }

    public DateTime getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj instanceof MqttClientSimulation){
            MqttClientSimulation client = (MqttClientSimulation) obj;
            return client.getTopic().equals(this.getTopic());
        }
        return false;
    }
}
