package de.othr.sw.WatchTHot.WatchTHotstarter.boundary.visualization;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Map problems --> therefor List --> MqttClients --> temperature/thermometer/etc etc
 */
@Getter
@NoArgsConstructor
@Component
public class BoundaryRoom {
    private long id;
    private List<MqttClientData> clientDataList = new ArrayList<>();
    private String name;


    public void setId(long id) {
        this.id = id;
    }

    public void setClientDataList(List<MqttClientData> clientDataList) {
        this.clientDataList = clientDataList;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public List<MqttClientData> getClientDataList() {
        return clientDataList;
    }

    public String getName() {
        return name;
    }
    public void addMqttClientData(MqttClientData data){
        if(!this.clientDataList.contains(data)){
            this.clientDataList.add(data);
        }

    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        } else if(obj instanceof BoundaryRoom){
            BoundaryRoom boundaryRoom = (BoundaryRoom) obj;
            return this.getId() == boundaryRoom.getId();
        }else{return false;}
    }
}
