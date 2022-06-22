package eist.eistbaer.reservationsystem.restaurant.address;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Address {
    @Id
    @GeneratedValue
    private Long id;

    private float latCoord;

    private float longCoord;

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

    public float getLatCoord() {
        return latCoord;
    }

    public void setLatCoord(float latCoord) {
        this.latCoord = latCoord;
    }

    public float getLongCoord() {
        return longCoord;
    }

    public void setLongCoord(float longCoord) {
        this.longCoord = longCoord;
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

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", latCoord=" + latCoord +
                ", longCoord=" + longCoord +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", postalCode=" + postalCode +
                ", country='" + country + '\'' +
                '}';
    }
}
