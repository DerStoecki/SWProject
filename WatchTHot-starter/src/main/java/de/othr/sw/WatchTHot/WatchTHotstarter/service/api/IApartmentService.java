package de.othr.sw.WatchTHot.WatchTHotstarter.service.api;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface IApartmentService {

    /**
     * Get the complete RoomService
     * @return the Roomservice.
     */
    IRoomService getRoomService();

    /**
     * Return all the MeterData the Apartment has
     * @return The MeterData List.
     */
    List<MqttClientData> getLatestMeterData();

    /**
     * Returns All the Apartments of the current User
     * @param loggedInUser the logged in User
     * @return List of apartments for selection for visualization.
     */
    List<Apartment> getApartments(User loggedInUser);

    /**
     * Called by visualizer Service, after User has logged in and selected an apartment
     * @param selectedApartment
     */
    void setCurrentApartment(Apartment selectedApartment);

    boolean removeApartmentFromUser(Apartment apartment, User user);
}
