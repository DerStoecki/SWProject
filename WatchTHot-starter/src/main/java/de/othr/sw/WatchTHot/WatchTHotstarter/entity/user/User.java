package de.othr.sw.WatchTHot.WatchTHotstarter.entity.user;

import de.othr.sw.WatchTHot.WatchTHotstarter.entity.rolemanagement.Salt;

import javax.persistence.*;
import java.util.List;

/**
 * The Actual User logging in to overview the Temperatures. NOT the mqttclient
 */
@Entity
@Table(name="USER", schema = "swwatchthot")
@SequenceGenerator(name = "user_generator", sequenceName = "user_generator", initialValue = 0)
@Access(AccessType.FIELD)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_generator")
    @Column(name = "USER_ID")
    private Long id;
    @Column(name = "USER_NAME")
    private String username;
    @Column(name = "PASSWORD")
    private String pwd;
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Column(name = "FAMILY_NAME")
    private String familyName;



    @ManyToMany
    private List<Apartment> apartments;
    @OneToOne
    private Salt salt;


    //CTOR
    public User(){}
    //Getter & Setter


    public User(String usernameId, String pwd) {
        this.id = usernameId;
        this.pwd = pwd;
        createSalt();
    }

    private void createSalt() {
    }

    public String getId() {
        return id;
    }

    public void setId(String usernameId) {
        this.id = usernameId;
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
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!obj.getClass().equals(User.class)) {
            return false;
        }
        User otherUser = (User) obj;
        return this.id.equals(otherUser.id);
    }
}
