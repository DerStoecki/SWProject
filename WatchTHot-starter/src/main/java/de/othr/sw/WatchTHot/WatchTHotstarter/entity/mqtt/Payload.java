package de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt;

import org.joda.time.DateTime;

import javax.persistence.*;

@Entity
@Table(name = "PAYLOAD", schema = "swwatchthot")
@Access(AccessType.FIELD)
public class Payload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PAYLOAD_ID")
    private Long id;
    @Column(name = "TOPIC")
    private String topic;
    @Column(name = "ENTRY")
    private String payloadEntry;
    @Column(name = "TIMESTAMP")
    private DateTime timeStamp;


    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if(!obj.getClass().equals(Payload.class)){
            return false;
        }
        Payload otherPayload = (Payload) obj;
        return this.id.equals(otherPayload.id);

    }
}
