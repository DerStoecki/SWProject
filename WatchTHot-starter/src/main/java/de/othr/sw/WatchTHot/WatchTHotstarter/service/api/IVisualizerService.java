package de.othr.sw.WatchTHot.WatchTHotstarter.service.api;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.exceptions.LoginFailException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IVisualizerService {
    IApartmentService getApartment();

    IUserService getUserService();

    boolean removeOfDifferentUserApartment(User user, Apartment apartment);

    boolean removeOwnApartment(Apartment apartment);

    User login(String username, String password) throws IOException, LoginFailException;

    void addApartmentUser(User value, Apartment apartment);

    void filterRooms();

    //temperatures
    Map<Room, List<MqttClientData>> getRoomTemperature();

    //meter
    Map<Room, List<MqttClientData>> getRoomMeter();

    MqttClientData getThermostat();

    void clear();

    boolean selectApartment(long id);

    void clearData();

    void updateData();

    Map<Room, List<MqttClientData>>  getRoomTemperatureForVisualizer();
    Map<Room, List<MqttClientData>> getRoomMeterForVisualizer();
}