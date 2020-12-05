package de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="TOPIC", schema = "swwatchthot" )
@Access(AccessType.FIELD)
public class Topic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TOPIC_ID")
    private Long id;
    @Column(name="TOPIC_NAME")
    private String topic;
    @ManyToOne
    private MqttClientData mqttClientData;
    @OneToMany
    private List<Payload> payloads;


    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!obj.getClass().equals(Topic.class)) {
            return false;
        }
        Topic otherTopic = (Topic) obj;
        return this.id.equals(otherTopic.id);
    }
}
