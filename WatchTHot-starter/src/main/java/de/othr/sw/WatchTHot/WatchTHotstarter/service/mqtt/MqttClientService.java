package de.othr.sw.WatchTHot.WatchTHotstarter.service.mqtt;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.DeviceType;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientSimulation;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IMqttClientService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MqttClientService implements IMqttClientService {

    private float setPointTemperature;
    private static final Random random = new Random();


    private List<MqttClientSimulation> dataSimulations;

    public MqttClientService() throws IOException {
        this.initJson(new String(Files.readAllBytes(Paths.get("/payloadsimulation/meter/heatmeter.json"))));

        this.initJson(new String(Files.readAllBytes(Paths.get("/payloadsimulation/meter/watermeter.json"))));

        this.initJson(new String(Files.
                readAllBytes(Paths.get("/payloadsimulation/temperatureSensor/temperatureSensorChildrenRoom.json"))));
        this.initJson(new String(Files.
                readAllBytes(Paths.get("/payloadsimulation/temperatureSensor/temperatureSensorKitchen.json"))));
        this.initJson(new String(Files.
                readAllBytes(Paths.get("/payloadsimulation/temperatureSensor/temperatureSensorLivingRoom.json"))));
        this.initJson(new String(Files.
                readAllBytes(Paths.get("/payloadsimulation/temperatureSensor/temperatureSensorParents.json"))));
        this.initJson(new String(Files.
                readAllBytes(Paths.get("/payloadsimulation/temperatureSensor/temperatureSensoUtilityRoom.json"))));
        this.initJson(new String(Files.
                readAllBytes(Paths.get("/payloadsimulation/thermostat/thermostat.json"))));

    }

    /**
     * Initialise all Jsons and Add Consumption
     * @param configuration json from config/Initial local jsons
     */
    private void initJson(String configuration){
        JsonObject jsonConfigObject = new Gson().fromJson(configuration, JsonObject.class);
        JsonObject payloads = jsonConfigObject.getAsJsonObject("payload");
        MqttClientSimulation client = new MqttClientSimulation(jsonConfigObject.get("topic").getAsString(), payloads,
                DeviceType.valueOf(jsonConfigObject.get("DeviceType").getAsString().toUpperCase()),
                jsonConfigObject.get("Room").getAsString(), jsonConfigObject.get("name").getAsString());
        if(!this.dataSimulations.contains(client)){
            this.dataSimulations.add(client);
        }
        if(!client.getDeviceType().equals(DeviceType.METER)){
            client.setConsumption(5);
        } else {
            client.setConsumption(0);
        }
        client.setTimeNow();
    }


    /**
     * Set the Temperature of Thermostate and Thermometer and increase the consumption (later important for Meter)
     * @param newTemperatureValue set By SuperUser
     * @return true on success
     */
    @Override
    public boolean setTemperature(float newTemperatureValue) {
        float oldTemperature = this.setPointTemperature;
        this.setPointTemperature = newTemperatureValue;
        //only 2 meter therefor no filter and usage of forEach -> eventhough runtime would be...3 or 4n and reduces to n...
        this.dataSimulations.forEach(mqttClientSimulation -> {
            if (!mqttClientSimulation.getDeviceType().equals(DeviceType.METER)){
                mqttClientSimulation.setConsumption(Math.max(mqttClientSimulation.getConsumption() + (int)Math.ceil(oldTemperature + setPointTemperature), 0));
                if(mqttClientSimulation.getDeviceType().equals(DeviceType.THERMOSTAT)){
                    mqttClientSimulation.getPayload().addProperty("temperature", newTemperatureValue);
                    mqttClientSimulation.setTimeNow();
                }
            }

        });
        changePayloadsTemperatureSensor();

        return true;
    }

    /**
     * Change Temperature Payloads every 20 seconds --> Temperature Flactuations between 0.1 and 1 °C -> or -0.1°C
     */
    @Scheduled(cron = "*/20 * * * * *")
    private void changePayloadsTemperatureSensor() {

        this.dataSimulations.stream().filter(mqttClientSimulation -> mqttClientSimulation.getDeviceType().
                equals(DeviceType.TEMPERATURE_SENSOR)).forEach(sensor->{
                    sensor.getPayload().addProperty("temperature", randomTemperature());
                    sensor.setTimeNow();
        });
    }

    /**
     * Return random Float, usually cllaed by changePayloadsTemperatureSensor()
     * @return a random float
     */
    private float randomTemperature() {
        float maxDifference = 1.0f;
        float minDifference = 0.1f;
        return random.nextFloat() * (maxDifference - minDifference)
                + (random.nextBoolean()? minDifference : minDifference *-1);

    }

    /**
     * Increase every Hour MeterConsumption
     */
    @Scheduled (cron = "* * */1 * * *")
    private void changeMeterConsumption(){
        AtomicInteger currentConsumption = new AtomicInteger(0);
    this.dataSimulations.stream().filter(client->!client.getDeviceType().equals(DeviceType.METER)).forEach(nonMeter->{
        currentConsumption.getAndAdd(nonMeter.getConsumption());
    });
    this.dataSimulations.stream().filter(client->client.getDeviceType().equals(DeviceType.METER)).forEach(meter->{
        meter.setConsumption(meter.getConsumption() + currentConsumption.get());
        meter.setTimeNow();
    });

    }

    /**
     * getThe Data Simulation -> For Server Service
     * @return this.dataSimulations;
     */
    @Override
    public List<MqttClientSimulation> getDataSimulations() {
        return dataSimulations;
    }
}
