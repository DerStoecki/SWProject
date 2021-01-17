package de.othr.sw.WatchTHot.WatchTHotstarter.service.application.externalService;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.DeviceType;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.mqtt.MqttClientData;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.Privilege;
import de.othr.sw.WatchTHot.WatchTHotstarter.entity.user.User;
import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IVisualizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

@RestController
public class MunicipalUtilityService {

    private IVisualizerService visu;


    @Autowired
    public MunicipalUtilityService(IVisualizerService visu){
        this.visu = visu;
    }

    /**
     * Base64Encoded username and pw
     * @param id requests MeterId
     * @param municipal username
     * @param password password
     * @return FoundMeter with corresponding id
     */
    @RequestMapping(value="restapi/municipal/apartment/meter/", method = RequestMethod.GET)
    public MqttClientData clientData(@PathParam("id") long id, @PathParam("municipalUtilityUsername") String municipal, @PathParam("municipalPw") String password){

        String decodedUsername = Arrays.toString(Base64.getDecoder().decode(municipal));
        String decodedPassword = Arrays.toString(Base64.getDecoder().decode(password));
        Optional<User> repoUser = this.visu.getUserService().getUserRepository().getUserByUsername(decodedUsername);
        try {
            if(repoUser.isPresent() && repoUser.get().passwordIdentical(decodedPassword) &&
                    repoUser.get().getPrivilege().equals(Privilege.READWRITESUPER)) {
                MqttClientData clientToGet = this.visu.getApartment().getRoomService().getClientById(id);
                if(clientToGet.getDeviceType().equals(DeviceType.METER)){
                    return clientToGet;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
            return null;
        }

    /*
     * In Theory...more functionality would be possible, such as creating users etc as a municipal.
     * utility or adding Apartments but...the other service using this service is not available...
     */
}
