package de.othr.sw.WatchTHot.WatchTHotstarter.entity.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The ApartementClass is a class representing the Home of the User.
 * It can have multiple floors and rooms.
 * In each Room there are multiple MqttClients (e.g. multiple thermometers and heaters etc)
 */
public class Apartment {
    private final Address address;
    private final List<Room> rooms = new ArrayList<>();
    private final List<User> residents = new ArrayList<>();

}
