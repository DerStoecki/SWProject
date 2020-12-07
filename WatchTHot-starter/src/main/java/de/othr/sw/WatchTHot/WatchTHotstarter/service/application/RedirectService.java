package de.othr.sw.WatchTHot.WatchTHotstarter.service.application;

import de.othr.sw.WatchTHot.WatchTHotstarter.service.api.IRedirectService;
import org.springframework.web.bind.annotation.RestController;

/**
 * This Service Redirects If somethings wrong or ...anything really...
 * https://www.baeldung.com/spring-redirect-and-forward
 */
@RestController
public class RedirectService implements IRedirectService {


    @Override
    public void redirectToLogin() {

    }
}
