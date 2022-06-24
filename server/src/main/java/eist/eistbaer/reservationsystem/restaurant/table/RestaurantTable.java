package eist.eistbaer.reservationsystem.restaurant.table;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RestaurantTable {
    @Id
    @GeneratedValue
    private Long id;

    private int x;

    private int y;

    private int capacity;

    public RestaurantTable() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "RestaurantTable{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", capacity=" + capacity +
                '}';
    }
}
