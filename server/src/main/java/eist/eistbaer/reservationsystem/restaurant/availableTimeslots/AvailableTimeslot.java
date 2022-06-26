package eist.eistbaer.reservationsystem.restaurant.availableTimeslots;

import eist.eistbaer.reservationsystem.restaurant.table.RestaurantTable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AvailableTimeslot {

    private LocalTime time;
    private List<RestaurantTable> tables;

    public AvailableTimeslot(LocalTime time) {
        this.time = time;
        this.tables = new ArrayList<>();
    }

    public void addTable(RestaurantTable restaurantTable) {
        tables.add(restaurantTable);
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public List<RestaurantTable> getTables() {
        return tables;
    }

    public void setTables(List<RestaurantTable> tables) {
        this.tables = tables;
    }
}
