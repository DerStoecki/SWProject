package de.othr.sw.WatchTHot.WatchTHotstarter.service.application.externalService;

import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IRetrogramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RetroGramService implements IRetrogramService {


    private RestTemplate restServiceClient;

    @Autowired
    public RetroGramService(RestTemplate restServiceClient){
        this.restServiceClient = restServiceClient;
    }

    @Override
    public void sendPicture(String encodedPicture) {
        String urlRetro = "http://retrogram:8080/sendPicutre";
        restServiceClient.postForObject(urlRetro, encodedPicture, String.class);
    }
}
