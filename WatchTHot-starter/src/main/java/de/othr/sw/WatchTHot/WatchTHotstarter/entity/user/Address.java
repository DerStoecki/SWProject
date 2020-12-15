package de.othr.sw.WatchTHot.WatchTHotstarter.entity.user;

import javax.persistence.*;

@Entity
@Table(name="ADDRESS", schema = "swwatchtthot")
@Access(AccessType.FIELD)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ADDRESS_ID")
    private Long id;
    @Column(name="STREET")
    private String street;
    @Column(name="NUMBER")
    private String apartementNo;
    @Column(name="POSTAL_CODE")
    private String postalCode;
    @Column(name = "CITY")
    private String city;

    public Address(String street, String apartementNo, String postalCode, String city) {
        this.street = street;
        this.apartementNo = apartementNo;
        this.postalCode = postalCode;
        this.city = city;
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
        if (!obj.getClass().equals(Address.class)) {
            return false;
        }
        Address otherAddress = (Address) obj;
        return this.id.equals(otherAddress.id);

    }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getApartementNo() {
        return apartementNo;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }
}
