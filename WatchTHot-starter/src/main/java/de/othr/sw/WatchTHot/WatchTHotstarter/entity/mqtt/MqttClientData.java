package de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.Statistic;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticIdentifier;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import org.joda.time.DateTimeZone;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MqttClientData DB will have Topic, Payload, DeviceType and ISO Timestamp
 */
@Entity
@Table(name="MQTTCLIENT", schema = "swwatchthot")
@Access(AccessType.FIELD)
public class MqttClientData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MQTT_CLIENT_ID")
    private Long id;
    @Column(name = "DEVICE_NAME")
    private String name;
    @Column(name="DEVICE_TYPE")
    private DeviceType deviceType;
    @ManyToOne
    private Room room;
    @OneToMany(mappedBy="mqttClientData", cascade = CascadeType.ALL)
    private List<Topic> topics = new ArrayList<>();
    @OneToMany(mappedBy = "client")
    private List<Statistic> statistics = new ArrayList<>();

    @Transient
    private static String format = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
    @Transient
    private static DateTimeZone timeZone = DateTimeZone.UTC;

    public MqttClientData() {
    }

    public MqttClientData(String name, DeviceType deviceType) {
        this.name = name;
        this.deviceType = deviceType;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(!obj.getClass().equals(MqttClientData.class)){
            return false;
        }
        MqttClientData otherClient = (MqttClientData) obj;
        return this.id.equals(otherClient.id);
    }

    public Long getId() {
        return id;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public Room getRoom() {
        return room;
    }

    public List<Topic> getTopics() {
        return Collections.unmodifiableList(this.topics);
    }

    public String getName() {
        return name;
    }

    public static String getFormat() {
        return format;
    }

    public static DateTimeZone getTimeZone() {
        return timeZone;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public boolean addTopic(Topic topic){
        if(!this.topics.contains(topic)){
            this.topics.add(topic);
            return true;
        }
        return false;
    }

    public List<Statistic> getStatistics() {
        return Collections.unmodifiableList(this.statistics);
    }

    public List<Statistic> getHourlyStatistic(){
        return this.statistics.stream().filter(statistic -> statistic.getIdentifier().equals(StatisticIdentifier.HOUR)).collect(Collectors.toUnmodifiableList());
    }

    public List<Statistic> getDailyStatistic(){
        return this.statistics.stream().filter(statistic -> statistic.getIdentifier().equals(StatisticIdentifier.DAY)).collect(Collectors.toUnmodifiableList());
    }

    public List<Statistic> getWeeklyStatistic(){
        return this.statistics.stream().filter(statistic -> statistic.getIdentifier().equals(StatisticIdentifier.WEEK)).collect(Collectors.toUnmodifiableList());
    }
    public List<Statistic> getMonthlyStatistic(){
        return this.statistics.stream().filter(statistic -> statistic.getIdentifier().equals(StatisticIdentifier.MONTH)).collect(Collectors.toUnmodifiableList());
    }
    public List<Statistic> getYearlyStatistic(){
        return this.statistics.stream().filter(statistic -> statistic.getIdentifier().equals(StatisticIdentifier.YEAR)).collect(Collectors.toUnmodifiableList());
    }
    public void addStatistic(Statistic statistic) {
        if (!this.getStatistics().contains(statistic)) {
            this.statistics.add(statistic);
        }
    }
    public void removeStatistic(Statistic statistic) {
        this.statistics.remove(statistic);
    }

    @Override
    public String toString() {
        return "MqttClientData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", deviceType=" + deviceType +
                ", room=" + room +
                ", topics=" + topics +
                ", statistics=" + statistics +
                '}';
    }
}
