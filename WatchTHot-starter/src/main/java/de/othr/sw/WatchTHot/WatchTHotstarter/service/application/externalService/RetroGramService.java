package de.othr.sw.WatchTHot.WatchTHotstarter.service.application.externalService;

import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IRetrogramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class RetroGramService implements IRetrogramService {


    private RestTemplate restServiceClient;

    @Autowired
    public RetroGramService(RestTemplate restServiceClient){
        this.restServiceClient = restServiceClient;
    }

    @Override
    public boolean sendPicture(String encodedPicture) {
        String urlRetro = "http://127.0.0.1:8080/sendPicutre";
        try {
            restServiceClient.postForObject(urlRetro, encodedPicture, String.class);
        } catch (RestClientException e){
            return false;
        }
        return true;
    }
}
