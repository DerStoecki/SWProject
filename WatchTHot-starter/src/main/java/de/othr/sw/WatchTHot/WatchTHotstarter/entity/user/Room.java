package de.othr.sw.WatchTHot.WatchTHotstarter.entity.user;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Topic;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.Statistic;

import javax.persistence.*;
import java.util.List;

/**
 * Room in apartments, has MqttClients.
 */
@Entity
@Table(name="ROOM", schema = "swwatchthot")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROOM_ID")
    private Long id;
    @Column(name="ROOM_NAME")
    private String roomName;
    @Column(name="FLOOR")
    private int floor;
    @OneToMany(mappedBy = "room")
    private List<MqttClientData> data;
    @ManyToOne
    private Apartment apartment;
    @OneToMany
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
}
