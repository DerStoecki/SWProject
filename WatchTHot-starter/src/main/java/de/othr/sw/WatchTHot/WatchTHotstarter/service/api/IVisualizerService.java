package de.othr.sw.WatchTHot.WatchTHotstarter.service.api;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;

public interface IVisualizerService {
    IApartmentService getApartment();
    IUserService getUserService();
    boolean removeOfDifferentUserApartment(User user, Apartment apartment);

    boolean removeOwnApartment(Apartment apartment);
}
