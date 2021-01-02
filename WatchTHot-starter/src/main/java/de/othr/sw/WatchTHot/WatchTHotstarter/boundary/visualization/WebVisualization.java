package de.othr.sw.WatchTHot.WatchTHotstarter.boundary.visualization;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IVisualizerService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.LoginFailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
            model.addAttribute("RoomList", this.visualizerService.getApartment().getRoomService().getRoomsByApartment(selectedApartment.get()));
            return "smarthome/rooms";
        } else{
            return "redirect:/smarthome";
        }

    }
}
