package eist.eistbaer.reservationsystem.restaurant.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import eist.eistbaer.reservationsystem.reservation.Reservation;
import eist.eistbaer.reservationsystem.restaurant.table.tableType.TableType;
import eist.eistbaer.reservationsystem.restaurant.table.tableType.TableTypeConverter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
/*@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")*/
public class RestaurantTable {
    @Id
    @GeneratedValue
    private Long id;

    @Convert(converter = TableTypeConverter.class)
    private TableType tableType;

    private int capacity;

    @OneToMany
    @JoinColumn(name="table_id")
    @JsonIgnore
    private List<Reservation> reservations;

    public RestaurantTable() {
    }

    public boolean isFreeBetween(LocalDate date, LocalTime from, LocalTime to) {
        for (Reservation r : reservations) {
            if (r.getDate().isEqual(date) && r.interferingWithTimes(from, to)) {
                return false;
            }
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TableType getTableType() {
        return tableType;
    }

    public void setTableType(TableType tableType) {
        this.tableType = tableType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantTable that = (RestaurantTable) o;
        return id.equals(that.id);
    }

    @Override
    public String toString() {
        return "RestaurantTable{" +
                "id=" + id +
                ", tableType=" + tableType +
                ", capacity=" + capacity +
                ", reservations=" + reservations +
                '}';
    }
}
