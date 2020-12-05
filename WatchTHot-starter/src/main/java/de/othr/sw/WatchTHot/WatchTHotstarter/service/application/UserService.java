package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

//TODO AccessCheck
// Register User
// Login User
// Change Password



@RestController
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //TODO
    @GetMapping("/login")
    public User login(@RequestParam(value = "name", defaultValue = "World") String input, String pwd) {
        //TODO Check if it is correct
        //TODO LOGIN
        return null;
    }

    @GetMapping("/register")
    public User register() throws IOException {
        //woher kommen die Daten zum User

        User user = new User("FooUser", "Pwd");

        //Verarbeitung, Prüfung, .... "Business-Logik"
        //do nothing! Simulation!

        //speichern in DB --> persistence
        user = userRepository.save(user);

        return user;

    }

    @GetMapping("/register/differentUser")
    public User registerDifferentUser() throws IOException {
        return new User("Foo", "Bar");
    }


}
