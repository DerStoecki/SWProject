package de.othr.sw.WatchTHot.WatchTHotstarter.boundary.visualization;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Apartment;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@NoArgsConstructor
public class WrapperApartment {
    private List<Apartment> apartments;
    private Apartment selectedApartment;
    private String selectedId;


    public List<Apartment> getApartments() {
        return apartments;
    }

    public void setApartments(List<Apartment> apartments) {
        this.apartments = apartments;
    }
    public void setSelectedApartment(Apartment apt){
        this.selectedApartment = apt;
    }

    public List<Room> getRooms(){
        if(this.selectedApartment !=null){
            return this.selectedApartment.getRooms();
        }
        return null;
    }


    public String getSelectedId() {
        return this.selectedId;
    }
}
