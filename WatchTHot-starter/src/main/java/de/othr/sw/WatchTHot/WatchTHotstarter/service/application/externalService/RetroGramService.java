package de.othr.sw.WatchTHot.WatchTHotstarter.service.application.externalService;

import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IRetrogramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

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
        try {
            restServiceClient.postForObject(urlRetro, encodedPicture, String.class);
        } catch (RestClientException e){
            e.printStackTrace();
        }
    }
}
