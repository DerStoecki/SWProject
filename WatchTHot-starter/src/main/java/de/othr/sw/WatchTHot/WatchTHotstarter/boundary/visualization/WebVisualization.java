package de.othr.sw.WatchTHot.WatchTHotstarter.boundary.visualization;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.Statistic;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticIdentifier;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Privilege;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IPictureContributorService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IVisualizerService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.application.PictureContributorService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.LoginFailException;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.PrivilegeToLowException;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.RegisterFailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


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
    private IPictureContributorService contributorService = new PictureContributorService();
    private List<MqttClientData> clientMeter;



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
        if(this.entityUser == null){
            return "redirect:/";
        }
        Optional<Apartment> selectedApartment = this.apartmentList.stream().filter(apartment->apartment.getId().equals(Long.valueOf(apartmentWrapper.getSelectedId()))).findFirst();
        if(selectedApartment.isPresent()){
            this.selectedApartment = selectedApartment.get();
            if(this.visualizerService.selectApartment(this.selectedApartment.getId())){
                return "redirect:/smarthome/rooms";
            }
        }
        return "redirect:/smarthome";
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

       roomLogic(model);

        return "smarthome/rooms";
    }

    private void roomLogic(Model model) {
       this.visualizerService.updateData();
        model.addAttribute("roomTemperatures", this.visualizerService.getRoomTemperatureForVisualizer());
        model.addAttribute("roomMeter", this.visualizerService.getRoomMeterForVisualizer());
        model.addAttribute("thermostat", this.visualizerService.getThermostat());
        List<MqttClientData> meter = new ArrayList<>();
                this.visualizerService.getRoomMeterForVisualizer().forEach((key,value)->{
                    meter.addAll(value.stream().filter(data->data.getYearlyStatistic().size()>0).collect(Collectors.toList()));
                });
        model.addAttribute("allMeter", meter);
        this.clientMeter = meter;
        if(this.entityUser.getPrivilege().getLevel()>=1){
            model.addAttribute("privilege", true);
            model.addAttribute("temperature", new BoundaryTempertaure());
            model.addAttribute("newUser", new BoundaryUser());
            model.addAttribute("passwordMatch", false);
        } else {
            model.addAttribute("privilege", false);
        }
    }

   // @Scheduled(cron = "*/10 * * * * *")
    private void refreshEverything(){
        if(this.roomModel!=null){
            this.roomLogic(this.roomModel);
        }
    }

    @PostMapping({"/smarthome/registerUser"})
    public String registerNewUser(BoundaryUser user, Model model){
        if(this.entityUser==null){
            return "redirect:/";
        }
        if(user.getPassword().equals(user.getCheckPassword())) {
            try {
                Optional<User> newRegisteredUser = this.visualizerService.getUserService().registerDifferentUser(user.getUsername(),
                        user.getPassword(), this.entityUser, Privilege.valueOf(user.getPrivilege().toUpperCase()));
                newRegisteredUser.ifPresent(value -> this.visualizerService.addApartmentUser(value, this.selectedApartment));
            } catch (RegisterFailException e) {
                System.out.println("User with username" + user.getUsername() + " already exists");
            } catch (IOException e) {
                System.out.println("Pepper not available");
            } catch (PrivilegeToLowException e) {
                System.out.println("Privilege to Low; Can't grand access to User: " + user.getUsername() + " the Privilege: " + Privilege.valueOf(user.getPrivilege().toUpperCase()));
            }
        }
        return rooms(model);
    }

    @PostMapping({"/logout"})
    public String logout(){
        if(this.entityUser==null) {
            return "redirect:/";
        }
        this.clear();
        return "redirect:/";
    }

    public void clear(){
        this.apartmentList.clear();
        this.entityUser = null;
        this.selectedApartment = null;
        this.visualizerService.clear();
        this.clientMeter.clear();
    }

   @PostMapping({"/setTemperature"})
    public String setTemperature(BoundaryTempertaure temperature){
        if(this.entityUser == null){
            return "redirect:/";
        }
       this.mqttService.setTemperature(Float.parseFloat(temperature.getTemp()));
       return "redirect:/smarthome/rooms";
    }

    @GetMapping({"/smarthome/refreshApartmentData"})
    public String refresh(Model map){
        if(this.entityUser!=null){
            this.roomLogic(map);
            return "redirect:/smarthome/rooms";
        }
        return "redirect:/";
    }
    @PostMapping({"/postStatistic/day"})
    public String postDayStatistic(){
        if(this.entityUser == null){
            return "redirect:/";
        }else {
            this.postStatistic(StatisticIdentifier.DAY);
            System.out.println("worked");
            return "redirect:/smarthome/rooms";
        }
    }
    @PostMapping({"/postStatistic/week"})
    public String postWeekStatistic(){
        if(this.entityUser == null){
            return "redirect:/";
        }else {
            this.postStatistic(StatisticIdentifier.WEEK);
            System.out.println("worked");
            return "redirect:/smarthome/rooms";
        }
    }
    @PostMapping({"/postStatistic/month"})
    public String postMonthStatistic(){
        if(this.entityUser == null){
            return "redirect:/";
        }else {
            this.postStatistic(StatisticIdentifier.MONTH);
            System.out.println("worked");
            return "redirect:/smarthome/rooms";
        }
    }
    @PostMapping({"/postStatistic/year"})
    public String postYearStatistic(){
        if(this.entityUser == null){
            return "redirect:/";
        }else {
            this.postStatistic(StatisticIdentifier.YEAR);
            System.out.println("worked");
            return "redirect:/smarthome/rooms";
        }
    }

    private void postStatistic(StatisticIdentifier identifier){

        List<Statistic> filteredStatistic = new ArrayList<>();
        this.clientMeter.forEach(meter->{
            filteredStatistic.add(this.visualizerService.getMostRecentStatisticFromClient(meter, identifier));
        });
        this.contributorService.sendStatistic(identifier, filteredStatistic);
    }
}
