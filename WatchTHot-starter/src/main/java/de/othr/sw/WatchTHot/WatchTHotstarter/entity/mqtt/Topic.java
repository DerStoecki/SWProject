package de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt;

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
    @OneToMany(mappedBy = "topic")
    private List<Payload> payloads;

    @Transient
    private Payload mostRecentPayload;


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

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public MqttClientData getMqttClientData() {
        return mqttClientData;
    }

    public List<Payload> getPayloads() {
        return payloads;
    }

    public Payload getMostRecentPayload(){
        return this.mostRecentPayload;
    }

    /**
     * Usually called by MQTT SERVER SERVICE!!!! (UPDATES PAYLOAD)
     * @param mostRecentPayload
     */
    public void setMostRecentPayload(Payload mostRecentPayload) {
        this.mostRecentPayload = mostRecentPayload;
    }
}