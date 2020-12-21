package de.othr.sw.WatchTHot.WatchTHotstarter;

import de.othr.sw.WatchTHot.WatchTHotstarter.repository.RoomRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IVisualizerService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.application.VisualizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class WatchTHotStarterApplication {

	public static void main(String[] args) {
		SpringApplication.run(WatchTHotStarterApplication.class, args);
	}


}
