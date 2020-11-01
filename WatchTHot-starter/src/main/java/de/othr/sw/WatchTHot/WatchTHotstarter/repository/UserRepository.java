package de.othr.sw.WatchTHot.WatchTHotstarter.repository;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository  extends CrudRepository<User,String> {
}
