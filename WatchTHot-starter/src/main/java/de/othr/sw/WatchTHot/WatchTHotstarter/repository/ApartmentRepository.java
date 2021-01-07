package de.othr.sw.WatchTHot.WatchTHotstarter.repository;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ApartmentRepository extends CrudRepository<Apartment, Long> {
    List<Apartment> getApartmentsByUsers(User user);
    Apartment getApartmentById(Long id);

}
