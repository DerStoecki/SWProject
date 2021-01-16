package de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
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
    private float data;
    @Column(name = "TIMESTAMP")
    private String timeStamp;
    //DAILY/MONTHLY/WEEKLY/ETC ETC
    @Column(name = "IDENTIFIER")
    private String identifier;
    @Column(name="CONSUMPTION_PERCENT")
    private float consumptionPercent;
    @Column(name="SAVED_ENERGY_PERCENT_DIFFERENCE")
    private float consumptionSavePercent;
    @ManyToOne
    private MqttClientData client;


    public Statistic(StatisticIdentifier identifier, MqttClientData client, DateTime time, float data, float consumptionPercent, float consumptionSave){
        this.data = data;
        this.client = client;
        this.timeStamp = time.toString();
        this.identifier = identifier.name();
        this.consumptionPercent = consumptionPercent;
        this.consumptionSavePercent = consumptionSave;
    }

    public Statistic() {

    }

    public float getData() {
        return data;
    }

    public DateTime getTimeStamp() {
        return DateTime.parse(timeStamp);
    }

    public StatisticIdentifier getIdentifier() {
        return StatisticIdentifier.valueOf(this.identifier);
    }

    public void setData(float data) {
        this.data = data;
    }

    public void setTimeStamp(DateTime timeStamp) {
        this.timeStamp = timeStamp.toString();
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public MqttClientData getClient() {
        return this.client;
    }

    public Long getId() {
        return id;
    }

    public float getConsumptionPercent() {
        return consumptionPercent;
    }

    public float getConsumptionSavePercent() {
        return consumptionSavePercent;
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
