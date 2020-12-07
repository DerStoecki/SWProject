package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Privilege;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IApartmentService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IUserService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IVisualizerService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.LoginFailException;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.NotLoggedInException;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.PrivilegeToLowException;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.RegisterFailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

@RestController
public class VisualizerService implements IVisualizerService {

    // TODO
    // Get Statistic --> Parse Further --> VIA ApartmentService-->RoomService-->StatisticOfDevice
    // Show DeviceData --> Get DeviceData --> EachRoom

    IUserService userService = new UserService();
    IApartmentService apartmentService = new ApartmentService();

    private Optional<User> loggedInUser;

    /**
     * Logs in the User
     *
     * @param username usually via REST, is the User
     * @param password usually via REST, password
     * @return empty if pwd incorrect or user cannot be found else returns User for this session.
     */
    @GetMapping("/login")
    public Optional<User> login(@RequestParam(value = "username", defaultValue = "user") String username,
                                @RequestParam(value = "password", defaultValue = "pwd") String password) throws LoginFailException {
        loggedInUser = this.userService.login(username, password);
        return loggedInUser;
    }

    //TODO MAKE IT FOR VISUALIZER --> BUTTON TO REGISTER USER SO YOU CAN'T ACCESS OTHERWISE...NORMALLY
    @GetMapping("/register")
    public boolean register(@RequestParam(value = "username", defaultValue = "User") String username,
                                   @RequestParam(value = "password", defaultValue = "Password") String password,
                                   @RequestParam(value = "privilege", defaultValue = "Read", required = false) String privilege) throws PrivilegeToLowException, NotLoggedInException, IOException, RegisterFailException {

        if(this.loggedInUser.isPresent()) {
            Privilege privilegeToAllow = Privilege.valueOf(privilege.toUpperCase());
            this.userService.registerDifferentUser(username, password, this.loggedInUser.get(), privilegeToAllow);
            return true;
        } else {
            //TODO redirect to Login!
            throw new NotLoggedInException();
        }
    }


    @GetMapping("/changePassword")
    public boolean changePassword(@RequestParam(value = "oldpassword") String oldPw, @RequestParam(value = "newPassword") String newPwd) throws NotLoggedInException {
        if(this.loggedInUser.isPresent()){
            this.userService.changePassword(oldPw, newPwd, loggedInUser.get());
            return true;
        } else {
            throw new NotLoggedInException();
        }
    }

    @Override
    public IApartmentService getApartment() {
        return this.apartmentService;
    }

    @Override
    public IUserService getUserService() {
        return this.userService;
    }


}
