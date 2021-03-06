package de.othr.sw.WatchTHot.WatchTHotstarter.entity.user;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The Actual User logging in to overview the Temperatures. NOT the mqttclient
 */
@Entity
@Table(name = "USER", schema = "swwatchthot")
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
    @Column(name = "PRIVILEGE")
    private Privilege privilege = Privilege.READ;
    @Column(name = "SALT")
    private final String salt = createSalt();
    //FROM STACK OVERFLOW https://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
    @Transient
    private static final Random RANDOM = new SecureRandom();
    @Transient
    private static final int ITERATIONS = 10000;
    @Transient
    private static final int KEY_LENGTH = 512;


    @ManyToMany
    private List<Apartment> apartments = new ArrayList<>();




    //CTOR
    public User() {
    }
    //Getter & Setter


    public User(String username, String pwd) throws IOException {
        this.username = username;

        String pass = (pwd + this.salt + getPepper());
        //https://www.baeldung.com/sha-256-hashing-java
        this.pwd = Hashing.sha256().hashString(pass, StandardCharsets.UTF_8).toString();
        this.firstName = firstName;
        this.familyName = familyName;
    }

    private static String getPepper() throws IOException {
        //Copied from my Bachelor Coding in:
        // https://github.com/DerStoecki/openems/blob/feature/MQTT/io.openems.edge.bridge.mqtt/src/io/openems/edge/bridge/mqtt/component/AbstractMqttComponent.java
        JsonObject jsonObject = new Gson().fromJson(new String(Files.readAllBytes(Paths.get("src/main/resources/static/applicationdata/Pepper.json"))), JsonObject.class);
        return jsonObject.get("Pepper").getAsString();
    }

    private static String createSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return Arrays.toString(salt);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public Privilege getPrivilege() {
        return privilege;
    }

    public List<Apartment> getApartments() {
        return apartments;
    }

    protected String getSalt() {
        return salt;
    }

    public Apartment getApartment(Address address) {
        AtomicReference<Apartment> apt = new AtomicReference<>();
        this.apartments.stream().filter(apartment -> apartment.getAddress().equals(address)).findFirst().ifPresent(apt::set);

        return apt.get();

    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) throws IOException {
        createSalt();
        //https://www.baeldung.com/sha-256-hashing-java
        this.pwd = Hashing.sha256().hashString(getHashedPwd(pwd), StandardCharsets.UTF_8).toString();
    }
    private String getHashedPwd(String password) throws IOException {
        return (password + this.salt + getPepper());
    }

    public void addApartment(Apartment apartment) {
        if(!this.apartments.contains(apartment)) {
            this.apartments.add(apartment);
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

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

    public void setPrivilege(Privilege privilegeToAllow) {
        this.privilege = privilegeToAllow;
    }

    public boolean passwordIdentical(String password) throws IOException {
        return this.pwd.equals(Hashing.sha256().hashString(getHashedPwd(password), StandardCharsets.UTF_8).toString());
    }

    public void removeApartment(Apartment apartment) {
        this.apartments.remove(apartment);
    }
}
