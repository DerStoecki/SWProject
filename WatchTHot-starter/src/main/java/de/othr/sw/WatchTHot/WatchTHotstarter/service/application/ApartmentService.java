package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.ApartmentRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IApartmentService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ApartmentService implements IApartmentService {

    @Autowired
    ApartmentRepository apartmentRepository;

    IRoomService roomService = new RoomService();
    private List<Apartment> apartmentList;
    private Apartment apartment;


    /**
     * Returns the roomService for Method-Usage.
     *
     * @return this.roomService.
     */
    @Override
    public IRoomService getRoomService() {
        return this.roomService;
    }

    /**
     * TODO AUSLAGERN IN EXTERNEN SERVICE --> METER DATA Complete oder lieber nur current? --> Abwägen
     * GGF Nur Clientdata Liste zurückgeben dann mappen auf payload bzw payload auslesen und dann für statistik etc nutzen;
     * TOPIC contains list of payloads....maybe put in room service who contains the mqttclientdataservice?
     *
     * @return A List of MqttClients;
     */
    @Override
    public List<MqttClientData> getLatestMeterData() {
        return null;
    }

    /**
     * Return the apartments of the logged in User for Selection
     *
     * @param loggedInUser the logged in User
     * @return List<Apartment> of current User
     */
    @Override
    public List<Apartment> getApartments(User loggedInUser) {
        this.apartmentList = loggedInUser.getApartments();
        return this.apartmentList;
    }

    /**
     * Sets the current apartment for RoomService important
     *
     * @param selectedApartment usually from visualizer Service.
     */
    @Override
    public void setCurrentApartment(Apartment selectedApartment) {
        this.apartment = selectedApartment;
        this.roomService.loadRooms(this.apartment);
    }

    /**
     *
     * @param apartmentToRemove the apartment
     * @param userToRemove      the userToRemove
     * @return return true if apartmentWasPresent
     */
    @Override
    @Transactional
    public boolean removeApartmentFromUser(Apartment apartmentToRemove, User userToRemove) {
        Optional<Apartment> apartmentOption = this.apartmentRepository.findById(apartmentToRemove.getId());
        apartmentOption.ifPresent(apartment -> {
            if (apartment.getUsers().size() > 0) {
                List<User> users = apartment.getUsers();
                users.stream().filter(user -> user.getId().equals(userToRemove.getId()))
                        .findFirst().stream().collect(Collectors.toList()).forEach(user -> {
                    user.removeApartment(apartment);
                });
            }
            this.apartmentRepository.save(apartment);
        });
        return apartmentOption.isPresent();
    }

}
