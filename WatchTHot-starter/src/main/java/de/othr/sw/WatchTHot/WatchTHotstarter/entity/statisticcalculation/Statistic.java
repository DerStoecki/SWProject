package de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Gets the Historical Data and calculates Averages per day/week/month/year
 */
@Entity
@Table(name = "STATISTIC", schema = "swwatchthot")
@Access(AccessType.FIELD)
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="STATISTIC_ID")
    private Long id;
    @Column(name = "DATA")
    private String data;
    @Column(name = "TIMESTAMP")
    private DateTime timeStamp;
    //DAILY/MONTHLY/WEEKLY/ETC ETC
    @Column(name = "IDENTIFIER")
    private String identifier;
    @ManyToOne
    private Room room;


    public Statistic(StatisticIdentifier identifier, Room room, DateTime time, String data){
        this.data = data;
        this.room = room;
        this.timeStamp = time;
        this.identifier = identifier.name();
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!obj.getClass().equals(Statistic.class)) {
            return false;
        }
        Statistic otherStatistic = (Statistic) obj;
        return this.id.equals(otherStatistic.id);
    }

}
