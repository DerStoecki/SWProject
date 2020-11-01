package de.othr.sw.WatchTHot.WatchTHotstarter.entity.user;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user")
public class User {
    @Id
    private String usernameId;
    private String pwd;
    //TODO MAKE CLASSES
    private String owning;
    private String address;
    private String rooms;
    private String devices;

    //CTOR
    public User(){}
    //Getter & Setter


    public User(String usernameId, String pwd) {
        this.usernameId = usernameId;
        this.pwd = pwd;
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
