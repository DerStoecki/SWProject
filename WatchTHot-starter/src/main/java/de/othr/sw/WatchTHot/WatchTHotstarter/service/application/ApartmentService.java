package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.ApartmentRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.MqttClientDataRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IApartmentService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ApartmentService implements IApartmentService {

    @Autowired
    ApartmentRepository apartmentRepository;

    MqttClientDataRepository clientDataRepository;

    IRoomService roomService = new RoomService();
    private List<Apartment> apartmentList;
    private Apartment apartment;


    /**
     * Returns the roomService for Method-Usage.
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
     * @return A List of MqttClients;
     */
    @Override
    public List<MqttClientData> getLatestMeterData() {
        return null;
    }

    /**
     * Return the apartments of the logged in User for Selection
     * @param loggedInUser the logged in User
     * @return List<Apartment> of current User
     */
    @Override
    public List<Apartment> getApartments(User loggedInUser) {
        this.apartmentList = this.apartmentRepository.getApartmentsByUsers(loggedInUser);
        return this.apartmentList;
    }

    /**
     * Sets the current apartment for RoomService important
     * @param selectedApartment usually from visualizer Service.
     */
    @Override
    public void setCurrentApartment(Apartment selectedApartment) {
        this.apartment = selectedApartment;
        this.roomService.loadRooms(this.apartment);
    }

    @Override
    public boolean removeApartmentFromUser(Apartment apartment, User user){
        

        return false;
    }

}
