package eist.eistbaer.reservationsystem.restaurant.type;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class RestaurantType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(unique = true)
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

    /*@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantType that = (RestaurantType) o;
        return Objects.equals(name, that.name);
    }*/
}
