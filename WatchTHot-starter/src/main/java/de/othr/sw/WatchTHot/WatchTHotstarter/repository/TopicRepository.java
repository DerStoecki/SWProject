package de.othr.sw.WatchTHot.WatchTHotstarter.repository;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Topic;
import org.springframework.data.repository.CrudRepository;

public interface TopicRepository extends CrudRepository<Topic, Long> {
}
