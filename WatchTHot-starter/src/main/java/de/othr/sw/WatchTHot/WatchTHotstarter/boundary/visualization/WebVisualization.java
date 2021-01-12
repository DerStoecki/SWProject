package de.othr.sw.WatchTHot.WatchTHotstarter.boundary.visualization;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.DeviceType;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Privilege;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IVisualizerService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.LoginFailException;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.PrivilegeToLowException;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.RegisterFailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import java.io.IOException;
import java.util.*;


/**
 * Visualizes the Frontend depending on access rights of User and what they want to see
 */
@Controller
public class WebVisualization {

    private final IVisualizerService visualizerService;
    private final IMqttService mqttService;
    private Environment environment;
    private String applicationMode;
    private User entityUser;
    private List<Apartment> apartmentList;
    private Apartment selectedApartment;
    private Model roomModel;
    //temperatures
    private Map<Room, List<MqttClientData>> roomTemperature = new HashMap<>();
    //meter
    private Map<Room, List<MqttClientData>> roomMeter = new HashMap<>();
    //Thermostat
    private MqttClientData thermostat;


    @Autowired
    public WebVisualization(IVisualizerService visualizerService, IMqttService mqttService, Environment environment){
        this.visualizerService = visualizerService;
        this.environment = environment;
        this.applicationMode = environment.getProperty("app-mode");
        this.mqttService = mqttService;
    }

    @RequestMapping({"/", "/index", "/login"})
    public String login(Model model){
        model.addAttribute("mode", applicationMode);
        model.addAttribute("user", new BoundaryUser());
        return "login/index";
    }
    //help from https://frontbackend.com/thymeleaf/spring-boot-bootstrap-thymeleaf-input-password
    @PostMapping({"/submit"})
    public String submit(BoundaryUser user, Model model){
        try {
           this.entityUser = this.visualizerService.login(user.getUsername(), user.getPassword());
        } catch (LoginFailException e) {
            model.addAttribute("LoginFail", "Username or Password incorrect");
            return "redirect:/";
        } catch (IOException e) {
            model.addAttribute("Pepper", "Couldn't Find local Pepper");
            return "redirect:/";
        }
        return "redirect:/smarthome";
    }

    @RequestMapping({"/smarthome"})
    public String smarthome(Model model){
        if(this.entityUser == null){
            return "redirect:/";
        } else {
            //TODO Logic
            this.apartmentList = this.visualizerService.getApartment().getApartments(this.entityUser);
            WrapperApartment wrapperApartment = new WrapperApartment();
            wrapperApartment.setApartments(this.apartmentList);
            model.addAttribute("ApartmentWrapper", wrapperApartment);
            model.addAttribute("IdOfApartment", "ID");
            return "smarthome/smarthome";
        }
    }

    @PostMapping({"/submitApartment"})
    public String submitApartment(WrapperApartment apartmentWrapper, Model model){
        Optional<Apartment> selectedApartment = this.apartmentList.stream().filter(apartment->apartment.getId().equals(Long.valueOf(apartmentWrapper.getSelectedId()))).findFirst();
        if(selectedApartment.isPresent()){
            this.selectedApartment = selectedApartment.get();
            return "redirect:/smarthome/rooms";
        } else{
            return "redirect:/smarthome";
        }

    }

    @RequestMapping({"/smarthome/rooms"})
    public String rooms(Model model){
        if(this.entityUser==null){
            return "redirect:/";
        }
        if(this.selectedApartment==null){
            return "redirect:/smarthome";
        }
        this.roomModel = model;

        List<Room> roomsOfApartment = this.visualizerService.getApartment().getRoomService().getRoomsByApartment(this.selectedApartment);
        filterRooms(roomsOfApartment);
        model.addAttribute("roomTemperatures", this.roomTemperature);
        model.addAttribute("roomMeter", this.roomMeter);
        model.addAttribute("thermostat", this.thermostat);
        if(this.entityUser.getPrivilege().getLevel()>=1){
            model.addAttribute("privilege", true);
            model.addAttribute("temperature", new BoundaryTempertaure());
            model.addAttribute("newUser", new BoundaryUser());
            model.addAttribute("passwordMatch", false);
        } else {
            model.addAttribute("privilege", false);
        }

        return "smarthome/rooms";
    }

    /**
     * Filter the rooms --> ClientData .---> DeviceTypes
     * @param roomsOfApartment roomList usually from Apartment --> visualizerService
     */
    private void filterRooms(List<Room> roomsOfApartment) {
        roomsOfApartment.forEach(room->{
            room.getData().forEach(data-> {
                if(data.getDeviceType().equals(DeviceType.TEMPERATURE_SENSOR)){
                        addMap(this.roomTemperature, room, data);
                    }
                else if(data.getDeviceType().equals(DeviceType.METER)){
                    addMap(this.roomMeter, room, data);
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

    @PostMapping({"/registerUser"})
    public String registerNewUser(BoundaryUser user, Model model){
        if(this.entityUser==null){
            return "redirect:/";
        }
        if(user.getPassword().equals(user.getCheckPassword())) {
            try {
                Optional<User> newRegisteredUser =this.visualizerService.getUserService().registerDifferentUser(user.getUsername(), user.getPassword(), this.entityUser, Privilege.valueOf(user.getPrivilege().toUpperCase()));
                newRegisteredUser.ifPresent(value -> this.visualizerService.addApartmentUser(value, this.selectedApartment));
            } catch (RegisterFailException e) {
                System.out.println("User with username" + user.getUsername() + " already exists");
            } catch (IOException e) {
                System.out.println("Pepper not available");
            } catch (PrivilegeToLowException e) {
                System.out.println("Privilege to Low; Can't grand access to User: " + user.getUsername() + " the Privilege: " + Privilege.valueOf(user.getPrivilege().toUpperCase()));
            }
        }
        return "redirect:/smarthome/rooms";
    }

    @PostMapping({"/logout"})
    public String logout(){
        this.clear();
        return "redirect:/";
    }

    public void clear(){
        this.apartmentList.clear();
        this.entityUser = null;
        this.selectedApartment = null;
        this.thermostat = null;
        this.roomTemperature.clear();
        this.roomMeter.clear();
    }

    @PostMapping({"/setTemperature"})
    public String setTemperature(BoundaryTempertaure temperature){
        if(this.entityUser == null){
            return "redirect:/";
        }
        this.mqttService.setTemperature(Float.parseFloat(temperature.getTemp()));
        return "redirect:/smarthome/rooms";
    }
}
