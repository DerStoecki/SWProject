package de.othr.sw.WatchTHot.WatchTHotstarter.repository;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Payload;
import org.springframework.data.repository.CrudRepository;

public interface PayloadRepository extends CrudRepository<Payload, Long> {
}
