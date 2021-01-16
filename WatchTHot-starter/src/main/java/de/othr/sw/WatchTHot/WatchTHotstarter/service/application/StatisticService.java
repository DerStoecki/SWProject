package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;


import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Topic;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.Statistic;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.statisticcalculation.StatisticIdentifier;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.MqttClientDataRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.repository.StatisticRepository;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IStatisticService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatisticService implements IStatisticService {


    private final StatisticRepository statisticRepository;
    private final MqttClientDataRepository clientDataRepository;
    /**
     * Only Rooms With Meter Data
     */
    private List<MqttClientData> currentClients = new ArrayList<>();


    @Autowired
    public StatisticService (StatisticRepository statisticRepository, MqttClientDataRepository clientDataRepository){
        this.statisticRepository = statisticRepository;
        this.clientDataRepository = clientDataRepository;
    }

    @Override
    public Statistic getStatistic(String time, MqttClientData clientData, StatisticIdentifier identifier) {
        MqttClientData freshClient = this.clientDataRepository.getMqttClientDataById(clientData.getId());
        List<Statistic> clientStatistics = freshClient.getStatistics();
        if(clientStatistics.isEmpty()){
            if(clientData.getTopics().size()>0){
                String payloadEntry = clientData.getTopics().get(0).getMostRecentPayload().getPayloadEntry();
                Statistic statistic = new Statistic(identifier, clientData, DateTime.now(),
                        Float.parseFloat(payloadEntry), 0,0);
                standardEmptyRoutine(clientData, payloadEntry, identifier);
                return statistic;
            }
        }
        return null;
    }


    /**
     * Reduced to only Meter (bc that's the only relevant thing)
     * @param clientList available clients
     */
    @Override
    public void loadRoomData(List<MqttClientData> clientList) {
        this.currentClients.clear();
        this.currentClients = clientList;
    }

    @Override
    public void calculateInitStatistic(MqttClientData data) {
        if(data.getTopics().size()>0){
            if(data.getTopics().get(0).getPayloads().size()>0){
                this.standardEmptyRoutineEverything(data, data.getTopics().get(0)
                        .getPayloads().get(data.getTopics().size()-1).getPayloadEntry());
            }
        }
    }

    @Override
    public Statistic getStatisticFromId(Long id) {
        return this.statisticRepository.getStatisticById(id);
    }

    private void standardEmptyRoutineEverything(MqttClientData data, String payloadEntry) {
        Arrays.stream(StatisticIdentifier.values()).forEach(identifier->{
            if(data.getStatistics().stream().noneMatch(statistic -> statistic.getIdentifier().equals(identifier)))
            this.standardEmptyRoutine(data, payloadEntry, identifier);
        });

    }

    //TODO CALCULATION
    //Help from https://riptutorial.com/spring/example/21209/cron-expression <-- Examples
    @Scheduled(cron = "0 0 * * * *")
    public void calculateHourlyMeterStatistic(){
            this.currentClients.forEach(clientData -> {
                List<Statistic> hourStatistic = getStatisticByIdentifier(clientData, StatisticIdentifier.HOUR);
                List<Topic> topics = clientData.getTopics();
                if(topics.size()>0){
                    String payloadEntry =  topics.get(0).getMostRecentPayload().getPayloadEntry();
                    if(hourStatistic.isEmpty()){
                        standardEmptyRoutine(clientData, payloadEntry, StatisticIdentifier.HOUR);
                    }
                    else {
                        Statistic newestStatistic = hourStatistic.get(hourStatistic.size()-1);
                        standardRoutine(clientData, payloadEntry, newestStatistic, StatisticIdentifier.HOUR);
                    }
                }
            });
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void calculateMeterDaily(){
        this.currentClients.forEach(clientData -> {
                    List<Statistic> dailyStatistic = getStatisticByIdentifier(clientData, StatisticIdentifier.DAY);
            List<Topic> topics = clientData.getTopics();
            if(topics.size()>0){
                String payloadEntry =  topics.get(0).getMostRecentPayload().getPayloadEntry();
                if(dailyStatistic.isEmpty()){
                    standardEmptyRoutine(clientData, payloadEntry, StatisticIdentifier.DAY);
                }
                else {
                    Statistic newestStatistic = dailyStatistic.get(dailyStatistic.size()-1);
                    standardRoutine(clientData, payloadEntry, newestStatistic, StatisticIdentifier.DAY);
                }
            }
        });

    }

    @Scheduled(cron = "59 59 23 * * SUN")
    public void calculateMeterWeekly(){
        this.currentClients.forEach(clientData -> {
            List<Statistic> weeklyStatistic = getStatisticByIdentifier(clientData, StatisticIdentifier.WEEK);
            List<Topic> topics = clientData.getTopics();
            if(topics.size()>0){
                String payloadEntry =  topics.get(0).getMostRecentPayload().getPayloadEntry();
                if(weeklyStatistic.isEmpty()){
                    standardEmptyRoutine(clientData, payloadEntry, StatisticIdentifier.WEEK);
                }
                else {
                    Statistic newestStatistic = weeklyStatistic.get(weeklyStatistic.size()-1);
                    standardRoutine(clientData, payloadEntry, newestStatistic, StatisticIdentifier.WEEK);
                }
            }
        });
    }

    //Do every month and if month is quarter or is year ---> extra statistic with identifier
    @Scheduled(cron = "0 0 0 1 * ?")
    public void calculateMeterMonthly(){
        this.currentClients.forEach(clientData -> {
            List<Statistic> monthStatistic = getStatisticByIdentifier(clientData, StatisticIdentifier.MONTH);
            List<Topic> topics = clientData.getTopics();
            if(topics.size()>0){
                String payloadEntry =  topics.get(0).getMostRecentPayload().getPayloadEntry();
                if(monthStatistic.isEmpty()){
                    standardEmptyRoutine(clientData, payloadEntry, StatisticIdentifier.MONTH);
                }
                else {
                    Statistic newestStatistic = monthStatistic.get(monthStatistic.size()-1);
                    standardRoutine(clientData, payloadEntry, newestStatistic, StatisticIdentifier.MONTH);
                }
            }
        });
    }

    private List<Statistic> getStatisticByIdentifier(MqttClientData clientData, StatisticIdentifier identifier) {
        return clientData.getStatistics().stream().
                filter(statistic -> statistic.getIdentifier().equals(identifier)).
                collect(Collectors.toList());
    }

    private void standardEmptyRoutine(MqttClientData clientData, String payloadEntry, StatisticIdentifier identifier) {
        Statistic statistic = new Statistic(identifier, clientData, DateTime.now(),
                Float.parseFloat(payloadEntry), 0,0);
        statisticRepository.save(statistic);
        clientData.addStatistic(statistic);
        clientDataRepository.save(clientData);
    }

    private void standardRoutine(MqttClientData clientData, String payloadEntry, Statistic newestStatistic, StatisticIdentifier identifier) {
        float difference =  Float.parseFloat(payloadEntry) - newestStatistic.getData();
        float differencePercent = newestStatistic.getData() * 100 / Float.parseFloat(payloadEntry);
        float savedEnergy = differencePercent - newestStatistic.getConsumptionPercent();
        Statistic statistic = new Statistic(identifier, clientData, DateTime.now(), difference, differencePercent, savedEnergy);
        statisticRepository.save(statistic);
    }


}
