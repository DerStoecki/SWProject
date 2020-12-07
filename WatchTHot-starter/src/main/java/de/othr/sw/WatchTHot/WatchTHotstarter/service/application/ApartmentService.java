package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IApartmentService;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IRoomService;

public class ApartmentService implements IApartmentService {
    @Override
    public IRoomService getRoomService() {
        return null;
    }

    @Override
    public String getMeterData() {
        return null;
    }
}
