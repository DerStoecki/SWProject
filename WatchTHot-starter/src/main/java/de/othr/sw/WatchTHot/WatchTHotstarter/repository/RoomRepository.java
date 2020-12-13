package de.othr.sw.WatchTHot.WatchTHotstarter.repository;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoomRepository extends CrudRepository<Room, Long> {
    /**
     * Gets a Room by Apartment
     * @param apartment the current selected Apartment, usually after user selects their apartment
     * @return the Room list
     */
    List<Room> getRoomsByApartment(Apartment apartment);
}
