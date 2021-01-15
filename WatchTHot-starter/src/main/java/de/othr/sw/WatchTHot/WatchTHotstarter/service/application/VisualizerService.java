package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.DeviceType;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Payload;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Privilege;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IApartmentService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IUserService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IVisualizerService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
public class VisualizerService implements IVisualizerService {

    private final IUserService userService;

    private final IApartmentService apartmentService;


    private User loggedInUser;
    private List<Apartment> apartmentList;
    private Apartment selectedApartment;
    private Map<Room, List<MqttClientData>> roomTemperatureMap = new HashMap<>();
    private Map<Room, List<MqttClientData>> roomMeterMap = new HashMap<>();
    private MqttClientData thermostat;

    @Autowired
    public VisualizerService(IUserService userService, IApartmentService apartmentService) {
        this.userService = userService;
        this.apartmentService = apartmentService;
        setupDummyApartment();
    }

    private void setupDummyApartment() {
        this.apartmentService.setDummyUsers(this.userService.getDummyUser(this.apartmentService.getDummyApartment()));
    }


    /**
     * Logs in the User; Fails if user either doesn't exist or pw is incorrect. If correct load Apartments with the logged in User.
     *
     * @param username usually via REST, is the User
     * @param password usually via REST, password
     * @return empty if pwd incorrect or user cannot be found else returns User for this session.
     */

    //public User login(@RequestParam(value = "username", defaultValue = "user") String username,
      //                          @RequestParam(value = "password", defaultValue = "pwd") String password) throws LoginFailException, IOException {
    public User login(String username, String password) throws IOException, LoginFailException {
        this.loggedInUser = this.userService.login(username, password);
        this.apartmentList = this.apartmentService.getApartments(this.loggedInUser);
        return loggedInUser;
    }

    @Override
    public void addApartmentUser(User user, Apartment apartment) {
        this.userService.addApartmentToUser(user,apartment);
        this.apartmentService.addUser(user, apartment);
    }

    @Override
    public void filterRooms() {
        this.selectedApartment.getRooms().forEach(room->{
            room.getData().forEach(data-> {
                if(data.getDeviceType().equals(DeviceType.TEMPERATURE_SENSOR)){
                    addMap(this.roomTemperatureMap, room, data);
                }
                else if(data.getDeviceType().equals(DeviceType.METER)){
                    addMap(this.roomMeterMap, room, data);
                }
                else if(data.getDeviceType().equals(DeviceType.THERMOSTAT)){
                    this.thermostat = data;
                }
            });
        });
    }
    private void addMap(Map<Room, List<MqttClientData>> map, Room room, MqttClientData clientData){

        if(map.containsKey(room)){
            if(!map.get(room).contains(clientData)){
                map.get(room).add(clientData);
            }
        } else {
            List<MqttClientData> clientDataList = new ArrayList<>();
            clientDataList.add(clientData);
            map.put(room, clientDataList);
        }
    }
    @Override
    public Map<Room, List<MqttClientData>> getRoomTemperature() {
        return this.roomTemperatureMap;
    }

    @Override
    public Map<Room, List<MqttClientData>> getRoomMeter() {
        return this.roomMeterMap;
    }

    @Override
    public MqttClientData getThermostat() {
        return this.thermostat;
    }

    @Override
    public void clear() {
       this.clearData();
        this.selectedApartment  = null;
        this.loggedInUser = null;
    }


    public boolean register(String username, String password, String privilege) throws PrivilegeToLowException, RegisterFailException, IOException, NotLoggedInException {
        if (this.loggedInUser != null) {
            Privilege privilegeToAllow = Privilege.valueOf(privilege.toUpperCase());
            this.userService.registerDifferentUser(username, password, this.loggedInUser, privilegeToAllow);
            return true;
        } else {
            //usually redirect to Login in webvisualization
            throw new NotLoggedInException();
        }
    }



    //public boolean changePassword(@RequestParam(value = "oldpassword") String oldPw, @RequestParam(value = "newPassword") String newPwd) throws NotLoggedInException, IOException, PasswordIncorrectException {
    public boolean changePassword(String oldPw, String newPwd) throws IOException, PasswordIncorrectException, NotLoggedInException {
        if (this.loggedInUser != null) {
            this.userService.changePassword(oldPw, newPwd, loggedInUser);
            return true;
        } else {
            throw new NotLoggedInException();
        }
    }

    /**
     * Used by Visualisation; get all apartments beforehand and then select Apartment by id
     *
     * @param id Apartmentid
     * @return true if it was successful
     */

    //@GetMapping("/selectApartment")
    //public boolean selectApartment(@RequestParam(value = "apartmentId") long id) {
    public boolean selectApartment(long id){
        Optional<Apartment> apartmentToSelect = this.apartmentList.stream().filter(apartments -> apartments.getId().equals(id)).findFirst();
        if (apartmentToSelect.isPresent()) {
            this.selectedApartment = apartmentToSelect.get();
            return true;
        }
        return false;
    }

    @Override
    public void clearData() {
        this.thermostat = null;
        this.roomTemperatureMap.clear();
        this.roomMeterMap.clear();
    }

    @Override
    public void updateData() {
        this.clearData();
        this.selectedApartment = this.apartmentService.getApartmentById(this.selectedApartment.getId());
        this.apartmentService.setCurrentApartment(this.selectedApartment);
        this.filterRooms();
    }

    public List<Apartment> getApartmentList() throws CannotDisplayApartmentsException {
        if(this.loggedInUser != null){
            return this.loggedInUser.getApartments();
        }
        throw new CannotDisplayApartmentsException();
    }

    @Override
    public IApartmentService getApartment() {
        return this.apartmentService;
    }

    @Override
    public IUserService getUserService() {
        return this.userService;
    }


    /**
     * Remove the Apartment of a User and remove Reference in apartment, if Privilege is at least READWRITESUPER
     * @param user the different User
     * @param apartment the apartment to remove
     * @return true if successful; false if privilege not high enough or Apartment cannot be found
     */
    @Override
    @Transactional
    public boolean removeOfDifferentUserApartment(User user, Apartment apartment) {
        if(this.loggedInUser.getPrivilege().getLevel() >= Privilege.READWRITESUPER.getLevel()){
            return this.apartmentService.removeApartmentFromUser(apartment, user);
        }
        return false;
    }

    /**
     * Remove your own apartment (maybe if you're moving out or something)
     * @param apartment the apartment to remove
     * @return true on success
     */
    @Override
    @Transactional
    public boolean removeOwnApartment(Apartment apartment) {
        return this.apartmentService.removeApartmentFromUser(apartment, this.loggedInUser);
    }


}
