package de.othr.sw.WatchTHot.WatchTHotstarter.boundary.visualization;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Room;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
@Getter
@Setter
public class WrapperRoom {
    private List<Room> roomList;
    private List<Room> displayableRooms;

    public WrapperRoom(){
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }
    public void setDisplayableRooms(List<Room> roomList){
        this.displayableRooms = roomList;
    }

    public List<Room> getDisplayableRooms() {
        return displayableRooms;
    }

    public void addDisplayableRoom(String name) {
        if (this.displayableRooms.stream().noneMatch(room -> room.getRoomName().equals(name))) {
            Optional<Room> existingRoom = this.roomList.stream().filter(room -> room.getRoomName().equals(name)).findFirst();
            existingRoom.ifPresent(room -> this.displayableRooms.add(room));
        }
    }
    public void removeDisplayableRoom(Room room){
        this.displayableRooms.remove(room);
    }
}
