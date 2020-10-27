package de.othr.sw.WatchTHot.WatchTHotstarter.service;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserService {


    private UserRepository userRepository;

    //TODO
    @GetMapping("/login")
    public User login(@RequestParam(value = "name", defaultValue = "World") String input, String pwd) {
        return null;
    }

    @GetMapping("/register")
    public User register(){
        //woher kommen die Daten zum User

        User user = new User("FooUser", "Pwd");

        //Verarbeitung, Prüfung, .... "Business-Logik"
        //do nothing! Simulation!

        //speichern in DB --> persistence
        user = userRepository.save(user);

        return user;

    }

}
