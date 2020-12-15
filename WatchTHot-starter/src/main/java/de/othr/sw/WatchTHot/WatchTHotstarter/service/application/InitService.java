package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This Class is here To Init Users, apartments, rooms Topics etc etc via JsonFile
 */
public class InitService {
    @Autowired
    PayloadRepository payloadRepository;
    @Autowired
    MqttClientDataRepository dataRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    StatisticRepostiory statisticRepostiory;
    @Autowired
    ApartmentRepository apartmentRepository;
}
