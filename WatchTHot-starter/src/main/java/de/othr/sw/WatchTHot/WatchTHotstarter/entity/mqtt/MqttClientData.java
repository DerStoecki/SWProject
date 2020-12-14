package de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import org.joda.time.DateTimeZone;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
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
    @Column(name="DEVICE_TYPE")
    private DeviceType deviceType;
    @ManyToOne
    private Room room;
    @OneToMany(mappedBy="mqttClientData")
    private List<Topic> topics;

    private static String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
    private static DateTimeZone timeZone = DateTimeZone.UTC;

    public MqttClientData() {
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
}
