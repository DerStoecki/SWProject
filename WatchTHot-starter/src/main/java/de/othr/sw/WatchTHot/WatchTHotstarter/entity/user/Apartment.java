package de.othr.sw.WatchTHot.WatchTHotstarter.entity.user;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
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

    @OneToMany(mappedBy = "apartment",cascade = {CascadeType.ALL})
    private List<Room> rooms = new ArrayList<>();

    @ManyToOne
    private Address address;

    @ManyToMany(mappedBy = "apartments", cascade = {CascadeType.ALL})
    List<User> users = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public List<Room> getRooms() {
        return Collections.unmodifiableList(this.rooms);
    }

    public Address getAddress() {
        return address;
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(this.users);
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
    public void addUser(User user){
        if(!this.users.contains(user)){
            this.users.add(user);
        }
    }

    public void removeUser(User user){
        this.users.remove(user);
    }

    public void addRoom(Room room){
        if(!this.rooms.contains(room)){
            this.rooms.add(room);
        }
    }

    public void removeRoom(Room room){
        this.rooms.remove(room);
    }

    public void setAddress(Address address){
        this.address = address;
    }
}
