package de.othr.sw.WatchTHot.WatchTHotstarter.entity.user;

import javax.persistence.*;
import java.util.List;

/**
 * The Actual User logging in to overview the Temperatures. NOT the mqttclient
 */
@Entity
@Table(name="user")
@SequenceGenerator(name = "user_generator", sequenceName = "user_generator", initialValue = 0)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_generator")
    private String usernameId;
    private String pwd;
    //TODO MAKE CLASSES
    private Apartment apartment;
    private String name;
    private String familyName;
    private String salt;


    //CTOR
    public User(){}
    //Getter & Setter


    public User(String usernameId, String pwd) {
        this.usernameId = usernameId;
        this.pwd = pwd;
        createSalt();
    }

    private void createSalt() {
    }

    public String getUsernameId() {
        return usernameId;
    }

    public void setUsernameId(String usernameId) {
        this.usernameId = usernameId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    //equals & hashCode überschreiben


    @Override
    public int hashCode() {
        return this.usernameId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!obj.getClass().equals(User.class)) {
            return false;
        }
        User otherStudent = (User) obj;
        return this.usernameId.equals(otherStudent.usernameId);
    }
}
