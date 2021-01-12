package de.othr.sw.WatchTHot.WatchTHotstarter.service.api;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.LoginFailException;

import java.io.IOException;

public interface IVisualizerService {
    IApartmentService getApartment();
    IUserService getUserService();
    boolean removeOfDifferentUserApartment(User user, Apartment apartment);

    boolean removeOwnApartment(Apartment apartment);
    User login(String username, String password) throws IOException, LoginFailException;

    void addApartmentUser(User value, Apartment apartment);
}
