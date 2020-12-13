package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Privilege;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IApartmentService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IUserService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IVisualizerService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
public class VisualizerService implements IVisualizerService {

    // TODO
    // Get Statistic --> Parse Further --> VIA ApartmentService-->RoomService-->StatisticOfDevice
    // Show DeviceData --> Get DeviceData --> EachRoom

    IUserService userService = new UserService();
    IApartmentService apartmentService = new ApartmentService();

    private User loggedInUser;
    private List<Apartment> apartmentList;
    private Apartment selectedApartment;

    /**
     * Logs in the User; Fails if user either doesn't exist or pw is incorrect. If correct load Apartments with the logged in User.
     *
     * @param username usually via REST, is the User
     * @param password usually via REST, password
     * @return empty if pwd incorrect or user cannot be found else returns User for this session.
     */
    @GetMapping("/login")
    public User login(@RequestParam(value = "username", defaultValue = "user") String username,
                                @RequestParam(value = "password", defaultValue = "pwd") String password) throws LoginFailException, IOException {
        this.loggedInUser = this.userService.login(username, password);
        this.apartmentList = this.apartmentService.getApartments(this.loggedInUser);
        return loggedInUser;
    }

    //TODO MAKE IT FOR VISUALIZER --> BUTTON TO REGISTER USER SO YOU CAN'T ACCESS OTHERWISE...NORMALLY
    @GetMapping("/register")
    public boolean register(@RequestParam(value = "username", defaultValue = "User") String username,
                            @RequestParam(value = "password", defaultValue = "Password") String password,
                            @RequestParam(value = "privilege", defaultValue = "Read", required = false) String privilege) throws PrivilegeToLowException, NotLoggedInException, IOException, RegisterFailException {

        if (this.loggedInUser != null) {
            Privilege privilegeToAllow = Privilege.valueOf(privilege.toUpperCase());
            this.userService.registerDifferentUser(username, password, this.loggedInUser, privilegeToAllow);
            return true;
        } else {
            //TODO redirect to Login!
            throw new NotLoggedInException();
        }
    }


    @GetMapping("/changePassword")
    public boolean changePassword(@RequestParam(value = "oldpassword") String oldPw, @RequestParam(value = "newPassword") String newPwd) throws NotLoggedInException, IOException, PasswordIncorrectException {
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

    @GetMapping("/selectApartment")
    public boolean selectApartment(@RequestParam(value = "apartmentId") long id) {
        Optional<Apartment> apartmentToSelect = this.apartmentList.stream().filter(apartments -> apartments.getId().equals(id)).findFirst();
        if (apartmentToSelect.isPresent()) {
            this.selectedApartment = apartmentToSelect.get();
            this.apartmentService.setCurrentApartment(this.selectedApartment);
            return true;
        }
        return false;
    }

    @GetMapping("/getApartments")
    public List<Apartment> getApartmentList() throws CannotDisplayApartmentsException {
        if(this.loggedInUser != null){
            return this.apartmentList;
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
            if(this.apartmentService.removeApartmentFromUser(apartment, user)) {
                this.userService.saveUserChanges(user);
                return true;
            }
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
        if(this.apartmentService.removeApartmentFromUser(apartment, this.loggedInUser)){
            this.userService.saveUserChanges(this.loggedInUser);
            return true;
        }
        return false;
    }


}
