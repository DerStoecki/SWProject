package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Address;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.AddressRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.ApartmentRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IApartmentService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApartmentService implements IApartmentService {


    private final ApartmentRepository apartmentRepository;
    private final AddressRepository addressRepository;
    private final IRoomService roomService;
    private Apartment dummyApartment;

    private List<Apartment> apartmentList;
    private Apartment currentApartment;
    @Autowired
    public ApartmentService(ApartmentRepository apartmentRepository, IRoomService roomService,
                                    AddressRepository addressRepository) throws IOException {
        this.apartmentRepository = apartmentRepository;
        this.roomService = roomService;
        this.addressRepository = addressRepository;
        createDummyApartment();
    }

    private void createDummyApartment() throws IOException {
        String addressConfig = new String(Files.readAllBytes(Paths.get("src/main/java/de/othr/sw/WatchTHot/WatchTHotstarter/service/application/initJson/address/AddressApartment0.json")));
        JsonObject jsonConfigObject = new Gson().fromJson(addressConfig, JsonObject.class);
        Optional<Apartment> existingApartment = Optional.ofNullable(this.apartmentRepository.getApartmentById(Long.parseLong("1")));
        if(existingApartment.isPresent()){
            this.dummyApartment = existingApartment.get();
            return;
        }
        Address address = new Address(jsonConfigObject.get("Street").getAsString(),
                jsonConfigObject.get("apartmentNumber").getAsString(), jsonConfigObject.get("postalCode").getAsString(),
                jsonConfigObject.get("City").getAsString());
        addressRepository.save(address);
        Apartment dummyApartment = new Apartment();
        dummyApartment.setAddress(address);
        apartmentRepository.save(dummyApartment);
        this.roomService.setDummyData(dummyApartment);
        this.roomService.getRoomsByApartment(dummyApartment).forEach(dummyApartment::addRoom);
        this.apartmentRepository.save(dummyApartment);
        this.dummyApartment = dummyApartment;
    }

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
        List<Apartment> apartment= new ArrayList<>();
        //unknown bug causes to add them twice to database
        this.apartmentList.forEach(ap->{
            if(!apartment.contains(ap)){
                apartment.add(ap);
            }
        });
        return apartment;
    }

    /**
     * Sets the current apartment for RoomService important
     *
     * @param selectedApartment usually from visualizer Service.
     */
    @Override
    public void setCurrentApartment(Apartment selectedApartment) {
        this.currentApartment = selectedApartment;
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
                    apartmentToRemove.removeUser(user);
                });
            }
        });
        return apartmentOption.isPresent();
    }

    @Override
    public void setDummyUsers(List<User> dummyUser) {
        dummyUser.forEach(user ->
                this.dummyApartment.addUser(user));
        this.apartmentRepository.save(this.dummyApartment);
    }

    @Override
    public void addUser(User user, Apartment apartment){
        apartment.addUser(user);
    }
    @Override
    public Apartment getDummyApartment() {
        this.dummyApartment = this.apartmentRepository.getApartmentById(this.dummyApartment.getId());
        return this.dummyApartment;
    }

    @Override
    public Apartment getApartmentById(Long id) {
        return this.apartmentRepository.getApartmentById(id);
    }

}
