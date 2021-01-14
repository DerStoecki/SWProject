package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.Statistic;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticIdentifier;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticType;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.StatisticRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IStatisticService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StatisticService implements IStatisticService {


    StatisticRepository statisticRepository;

    Map<Room, List<Statistic>> roomStatisticMap = new ConcurrentHashMap<>();

    @Autowired
    public StatisticService (StatisticRepository statisticRepository){
        this.statisticRepository = statisticRepository;
    }

    @Override
    public String getStatistic(StatisticType type, String time, Room room, StatisticIdentifier identifier) {
        return null;
    }

    @Override
    public void loadRooms(List<Room> roomList) {
          /*  roomList.forEach(room -> {
                //Clientdata is clientdata, doesn't matter which topic
                if(room.getStatistics().isEmpty() && !room.getData().isEmpty()){
                    Statistic statistic = new Statistic(StatisticIdentifier.HOUR, room,
                            DateTime.now(), room.getData().get(0).getTopics().get(0).getPayloads().get(0).getPayloadEntry());
                    room.addStatistic(statistic);
                    this.statisticRepository.save(statistic);
                }
            });*/
    }
    //TODO CALCULATION
    //Help from https://riptutorial.com/spring/example/21209/cron-expression <-- Examples
   /* @Scheduled(cron = "0 0 * * * *")
    private void calculateHourlyStatistic(){
        DateTime now = DateTime.now();

    }
    @Scheduled(cron = "0 0 0 * * *")
    private void calculateDaily(){}
    @Scheduled(cron = "59 59 23 * * SUN")
    private void calculateWeekly(){}

    //Do every month and if month is quarter or is year ---> extra statistic with identifier
    @Scheduled(cron = "0 0 0 1 * ")
    private void calculateMonthly(){}
    */

}
