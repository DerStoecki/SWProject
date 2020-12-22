package de.othr.sw.WatchTHot.WatchTHotstarter.repository;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface TopicRepository extends CrudRepository<Topic, Long> {
}
