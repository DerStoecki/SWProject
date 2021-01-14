package de.othr.sw.WatchTHot.WatchTHotstarter.repository;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.DeviceType;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import org.springframework.data.repository.CrudRepository;

public interface MqttClientDataRepository extends CrudRepository<MqttClientData, Long> {
    MqttClientData getMqttClientDataByDeviceTypeAndRoom(DeviceType deviceType, Room room);
    MqttClientData getMqttClientDataByName(String name);
    MqttClientData getMqttClientDataById(Long id);
}
