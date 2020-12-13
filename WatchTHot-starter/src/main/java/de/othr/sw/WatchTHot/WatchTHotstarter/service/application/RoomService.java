package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticType;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.RoomRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IRoomService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class RoomService implements IRoomService {

    @Autowired
    RoomRepository roomRepository;

    private List<Room> roomList;

    @Override
    public String getStatistic(StatisticType type, String time, Room room) {
        return null;
    }

    @Override
    public void loadRooms(Apartment apartment) {
        this.roomList = this.roomRepository.getRoomsByApartment(apartment);
    }

    /*
    TODO Include ClientData
     Statistic Service
     Refresh Room
     Create Statistics routine on Yearly/Monthly etc etc etc...

     */
}
