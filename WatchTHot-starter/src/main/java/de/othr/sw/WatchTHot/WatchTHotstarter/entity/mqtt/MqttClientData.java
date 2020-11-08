package de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * MqttClientData DB will have Topic, Payload, DeviceType and ISO Timestamp
 */
public class MqttClientData {
    private final String topic;
    private String payload;
    private DeviceType deviceType;
    private DateTime time;
    private static String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
    private static DateTimeZone timeZone = DateTimeZone.UTC;

    public MqttClientData(String topic, String payload, DeviceType deviceType) {
        this.topic = topic;
        this.payload = payload;
        this.deviceType = deviceType;
        //Initial Time
        this.time = DateTime.now(timeZone);
    }
}
