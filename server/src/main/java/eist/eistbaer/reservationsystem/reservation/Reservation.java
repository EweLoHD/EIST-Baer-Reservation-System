package eist.eistbaer.reservationsystem.reservation;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eist.eistbaer.reservationsystem.reservation.util.ReservationDeserializer;
import eist.eistbaer.reservationsystem.restaurant.Restaurant;
import eist.eistbaer.reservationsystem.restaurant.table.RestaurantTable;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonPropertyOrder({ "clientName", "clientEmail", "fromTime", "toTime", "date", "people", "confirmed", "confirmationMailSent", "table", "restaurant" })
@JsonDeserialize(using = ReservationDeserializer.class)
public class Reservation {
    public static final int DEFAULT_DURATION = 2;

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;

    private String clientName;
    private String clientEmail;

    private LocalTime fromTime;
    private LocalDate date;

    private int people;

    private boolean confirmed = false;
    private boolean confirmationMailSent = false;

    @ManyToOne
    private RestaurantTable table;

    @ManyToOne()
    private Restaurant restaurant;

    public Reservation() {
    }

    /**
     * Checks if a certain Timestamp (from - to) overlaps with the Timestamp of the reservation
     * @param fromTime Start time to the Timestamp
     * @param toTime End time to the Timestamp
     * @return true if the Timestamp (from - to) overlaps with the Timeslot of the Reservation
     */
    public boolean interferingWithTimes(LocalTime fromTime, LocalTime toTime) {
        LocalDateTime reservationFromTime = this.getFromTime().atDate(LocalDate.now());
        LocalDateTime reservationToTime = this.getToTime().atDate(this.getFromTime().isBefore(this.getToTime()) ? LocalDate.now() : LocalDate.now().plusDays(1));

        LocalDateTime from = fromTime.atDate(LocalDate.now());
        LocalDateTime to = toTime.atDate(fromTime.isBefore(toTime) ? LocalDate.now() : LocalDate.now().plusDays(1));

        return (reservationFromTime.isBefore(from) && reservationToTime.isAfter(from))
                || (reservationFromTime.isBefore(to) && reservationToTime.isAfter(to))
                || (reservationFromTime.isAfter(from) && reservationToTime.isBefore(to))
                || (reservationFromTime.isBefore(from) && reservationToTime.isAfter(to))
                || reservationFromTime.equals(from)
                || reservationToTime.equals(to);
    }

    /**
     * Checks if all the values of the Reservation are correct:
     *  - Checks if a reservation at the given time is possible (if the provided table is free for the given time and date and if the restaurant has open at the given time and date).
     *  - Checks if the provided table belongs to the provided restaurant
     *  - Checks if the date and time doesn't lie in the past
     *  - Checks if table has the necessary capacity
     * @return true if the reservation is valid
     */
    @JsonIgnore
    public boolean isValid() {
        // Check if table belongs to the restaurant
        if (!restaurant.getRestaurantTables().contains(getTable())) return false;

        // Check if table is free
        for (Reservation r : table.getReservations()) {
            if (r.interferingWithTimes(getFromTime(), getToTime())) {
                return false;
            }
        }

        // Check if restaurant has open at time and date
        if (!restaurant.hasOpened(getDate(), getFromTime(), getToTime())) return false;

        // Check if the date and time doesn't lie in the past
        if ((getFromTime().isBefore(LocalTime.now()) && getFromTime().isAfter(LocalTime.of(4, 0))) || getDate().isBefore(LocalDate.now())) return false;

        // Check if table has the necessary capacity
        if (getTable().getCapacity() < getPeople()) return false;

        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
         return fromTime != null ? fromTime.plusHours(DEFAULT_DURATION) : null;
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

    public void setRestaurant(int restaurantID) {
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isConfirmationMailSent() {
        return confirmationMailSent;
    }

    public void setConfirmationMailSent(boolean confirmationMailSent) {
        this.confirmationMailSent = confirmationMailSent;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", clientName='" + clientName + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                ", fromTime=" + fromTime +
                ", date=" + date +
                ", people=" + people +
                ", confirmed=" + confirmed +
                ", confirmationMailSent=" + confirmationMailSent +
                ", table=" + table +
                ", restaurant=" + restaurant +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        return id.equals(that.id);
    }

}
