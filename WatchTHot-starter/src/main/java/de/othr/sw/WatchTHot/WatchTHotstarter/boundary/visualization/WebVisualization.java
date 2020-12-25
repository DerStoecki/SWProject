package de.othr.sw.WatchTHot.WatchTHotstarter.boundary.visualization;

import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IVisualizerService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Visualizes the Frontend depending on access rights of User and what they want to see
 */
@Controller
public class WebVisualization {

    private final IVisualizerService visualizerService;
    private final IMqttService mqttService;
    private Environment environment;
    private String applicationMode;


    @Autowired
    public WebVisualization(IVisualizerService visualizerService, IMqttService mqttService, Environment environment){
        this.visualizerService = visualizerService;
        this.environment = environment;
        this.applicationMode = environment.getProperty("app-mode");
        this.mqttService = mqttService;
    }

    @RequestMapping("/")
    public String index(Model model){
        model.addAttribute("datetime", new DateTime());
        model.addAttribute("username", "Felix");
        model.addAttribute("mode", applicationMode);

        return "index";
    }
}
