package de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt;

import org.joda.time.DateTime;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Transactional
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
    @OneToMany(mappedBy = "topic", cascade = {CascadeType.ALL})
    private List<Payload> payloads = new ArrayList<>();

    public Topic(String topic) {
        this.topic = topic;
    }

    public Topic(){}

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
       return this.getPayloads().get(this.getPayloads().size()-1);
    }

    public boolean addPayload(Payload payload){
        if(!this.payloads.contains(payload)) {
            this.payloads.add(payload);
            return true;
        }

        return false;
    }

    public void setMqttClientData(MqttClientData mqttClientData) {
        this.mqttClientData = mqttClientData;
    }


}
