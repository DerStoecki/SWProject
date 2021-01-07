package de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import org.joda.time.DateTimeZone;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MqttClientData DB will have Topic, Payload, DeviceType and ISO Timestamp
 */
@Entity
@Table(name="MQTTCLIENT", schema = "swwatchthot")
@Access(AccessType.FIELD)
public class MqttClientData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MQTT_CLIENT_ID")
    private Long id;
    @Column(name = "DEVICE_NAME")
    private String name;
    @Column(name="DEVICE_TYPE")
    private DeviceType deviceType;
    @ManyToOne
    private Room room;
    @OneToMany(mappedBy="mqttClientData", cascade = CascadeType.ALL)
    private List<Topic> topics = new ArrayList<>();
    @Transient
    private static String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
    @Transient
    private static DateTimeZone timeZone = DateTimeZone.UTC;

    public MqttClientData() {
    }

    public MqttClientData(String name, DeviceType deviceType) {
        this.name = name;
        this.deviceType = deviceType;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(!obj.getClass().equals(MqttClientData.class)){
            return false;
        }
        MqttClientData otherClient = (MqttClientData) obj;
        return this.id.equals(otherClient.id);
    }

    public Long getId() {
        return id;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public Room getRoom() {
        return room;
    }

    public List<Topic> getTopics() {
        return Collections.unmodifiableList(this.topics);
    }

    public static String getFormat() {
        return format;
    }

    public static DateTimeZone getTimeZone() {
        return timeZone;
    }

    @Transactional
    public void setRoom(Room room) {
        this.room = room;
    }

    @Transactional
    public boolean addTopic(Topic topic){
        if(!this.topics.contains(topic)){
            this.topics.add(topic);
            return true;
        }
        return false;
    }

}
