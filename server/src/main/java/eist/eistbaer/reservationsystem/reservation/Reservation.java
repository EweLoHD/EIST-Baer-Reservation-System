package eist.eistbaer.reservationsystem.reservation;

import eist.eistbaer.reservationsystem.restaurant.Restaurant;
import eist.eistbaer.reservationsystem.restaurant.table.RestaurantTable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    private String clientName;
    private String clientEmail;

    private LocalTime fromTime;
    private LocalTime toTime;

    private LocalDate date;

    @ManyToOne
    private RestaurantTable table;

    @ManyToOne
    private Restaurant restaurant;

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public void setFromTime(LocalTime fromTime) {
        this.fromTime = fromTime;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public void setToTime(LocalTime toTime) {
        this.toTime = toTime;
    }

    public RestaurantTable getTable() {
        return table;
    }

    public void setTable(RestaurantTable table) {
        this.table = table;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                ", date=" + date +
                ", table=" + table +
                ", restaurant=" + restaurant +
                '}';
    }
}
