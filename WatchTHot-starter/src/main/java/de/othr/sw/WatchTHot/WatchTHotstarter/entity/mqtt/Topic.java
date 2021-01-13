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

    @Transient
    private Payload mostRecentPayload;

    public Payload getMostRecentPayload(){
        if(this.mostRecentPayload==null){
            this.getPayloads().forEach(payload -> {
                if(this.mostRecentPayload == null){
                    this.mostRecentPayload = payload;
                } else {
                    DateTime time = new DateTime(this.mostRecentPayload.getTimeStamp());
                    DateTime payloadTime = new DateTime(payload.getTimeStamp());
                    if(payloadTime.isAfter(time)){
                        this.mostRecentPayload = payload;
                    }
                }
            });
        }
        return this.mostRecentPayload;
    }

    /**
     * Usually called by MQTT SERVER SERVICE!!!! (UPDATES PAYLOAD)
     * @param mostRecentPayload
     */
    public void setMostRecentPayload(Payload mostRecentPayload) {
        this.mostRecentPayload = mostRecentPayload;
    }

    @Transactional
    public boolean addPayload(Payload payload){
        if(!this.payloads.contains(payload)) {
            this.payloads.add(payload);
            return true;
        }

        return false;
    }
    @Transactional
    public void setMqttClienspringtData(MqttClientData mqttClientData) {
        this.mqttClientData = mqttClientData;
    }
}
