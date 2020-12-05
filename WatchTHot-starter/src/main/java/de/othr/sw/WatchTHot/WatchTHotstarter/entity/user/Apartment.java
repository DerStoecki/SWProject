package de.othr.sw.WatchTHot.WatchTHotstarter.entity.user;

import javax.persistence.*;
import java.util.List;

/**
 * The ApartementClass is a class representing the Home of the User.
 * It can have multiple floors and rooms.
 * In each Room there are multiple MqttClients (e.g. multiple thermometers and heaters etc)
 */
@Entity
@Table(name="APARTMENT", schema = "swwatchthot")
@Access(AccessType.FIELD)
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="APARTMENT_ID")
    private Long id;

    @OneToMany(mappedBy = "apartment")
    private List<Room> rooms;

    @ManyToOne
    private Address address;

    @ManyToMany(mappedBy = "apartments")
    List<User> users;

    public Long getId() {
        return id;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Address getAddress() {
        return address;
    }

    public List<User> getUsers() {
        return users;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!obj.getClass().equals(Apartment.class)) {
            return false;
        }
        Apartment otherApartment = (Apartment) obj;
        return this.id.equals(otherApartment.id);
    }



}
