package de.othr.sw.WatchTHot.WatchTHotstarter.entity.user;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.Statistic;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

/**
 * Room in apartments, has MqttClients.
 */
@Entity
@Table(name = "ROOM", schema = "swwatchthot")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROOM_ID")
    private Long id;
    @Column(name = "ROOM_NAME")
    private String roomName;
    @Column(name = "FLOOR")
    private int floor;
    @OneToMany(mappedBy = "room")
    private List<MqttClientData> data;
    @ManyToOne
    private Apartment apartment;
    @OneToMany(mappedBy = "room")
    private List<Statistic> statistics;

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!obj.getClass().equals(Room.class)) {
            return false;
        }
        Room otherRoom = (Room) obj;
        return this.id.equals(otherRoom.id);
    }

    public Long getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getFloor() {
        return floor;
    }

    public List<MqttClientData> getData() {
        return Collections.unmodifiableList(this.data);
    }

    public Apartment getApartment() {
        return apartment;
    }

    public List<Statistic> getStatistics() {
        return Collections.unmodifiableList(this.statistics);
    }

    @Transactional
    public void addStatistic(Statistic statistic) {
        if (!this.getStatistics().contains(statistic)) {
            this.statistics.add(statistic);
        }
    }

    @Transactional
    public void addData(MqttClientData data) {
        if (!this.data.contains(data)) {
            this.data.add(data);
        }
    }

    @Transactional
    public void removeStatistic(Statistic statistic) {
        this.statistics.remove(statistic);
    }

    @Transactional
    public void removeData(MqttClientData data){
        this.data.remove(data);
    }
}
