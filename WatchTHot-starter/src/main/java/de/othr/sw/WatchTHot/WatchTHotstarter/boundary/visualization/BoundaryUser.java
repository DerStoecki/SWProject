package de.othr.sw.WatchTHot.WatchTHotstarter.boundary.visualization;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@Component
public class BoundaryUser {
    private String username;
    private String password;
    private String privilege;
    private String checkPassword;
    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return password;
    }

    public String getPrivilege() {
        return privilege;
    }

    public String getCheckPassword() {
        return checkPassword;
    }
}
