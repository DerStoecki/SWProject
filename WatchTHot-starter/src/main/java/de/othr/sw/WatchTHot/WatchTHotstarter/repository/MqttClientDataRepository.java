package de.othr.sw.WatchTHot.WatchTHotstarter.repository;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import org.springframework.data.repository.CrudRepository;

public interface MqttClientDataRepository extends CrudRepository<MqttClientData, Long> {
}
