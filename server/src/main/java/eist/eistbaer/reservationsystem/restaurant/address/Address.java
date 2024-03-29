package eist.eistbaer.reservationsystem.restaurant.address;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Address {
    @Id
    @GeneratedValue
    private Long id;

    private float lat;

    private float lon;

    private String street;

    private String city;

    private int postalCode;

    private String country;


    public Address() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int calculateDistance(Address other) {
        float lat2 = other.getLat();
        float lon2 = other.getLon();

        var p = 0.017453292519943295;    // Math.PI / 180
        var a = 0.5 - Math.cos((lat2 - lat) * p)/2 + Math.cos(lat * p) * Math.cos(lat2 * p) * (1 - Math.cos((lon2 - lon) * p))/2;

        return (int) Math.round(12742 * Math.asin(Math.sqrt(a))); // 2 * R; R = 6371 km
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", postalCode=" + postalCode +
                ", country='" + country + '\'' +
                '}';
    }
}
