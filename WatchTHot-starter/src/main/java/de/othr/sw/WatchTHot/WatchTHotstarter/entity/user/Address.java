package de.othr.sw.WatchTHot.WatchTHotstarter.entity.user;

public class Address {

    private final String street;
    private final int apartementNo;
    private final String postalCode;
    private final String city;

    public Address(String street, int apartementNo, String postalCode, String city) {
        this.street = street;
        this.apartementNo = apartementNo;
        this.postalCode = postalCode;
        this.city = city;
    }
}
