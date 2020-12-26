package de.othr.sw.WatchTHot.WatchTHotstarter.boundary.visualization;

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

import java.awt.event.HierarchyBoundsAdapter;
import java.io.IOException;

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

            return "smarthome/smarthome";
        }
    }
}
