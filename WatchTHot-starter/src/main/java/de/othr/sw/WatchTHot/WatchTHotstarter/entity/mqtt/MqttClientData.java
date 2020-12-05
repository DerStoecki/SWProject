package de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import org.joda.time.DateTimeZone;

import javax.persistence.*;
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
}
