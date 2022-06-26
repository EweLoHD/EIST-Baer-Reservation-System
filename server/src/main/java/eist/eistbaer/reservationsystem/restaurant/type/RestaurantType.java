package eist.eistbaer.reservationsystem.restaurant.type;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;

@Entity
public class RestaurantType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonValue
    private String name;

    public RestaurantType() {
    }

    public RestaurantType(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "RestaurantType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
