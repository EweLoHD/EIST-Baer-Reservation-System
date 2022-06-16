package eist.eistbaer.reservationsystem.restaurant.pictures;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class RestaurantPicture {

    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    public RestaurantPicture() {
    }

    public RestaurantPicture(String fileName) {
        this.fileName = fileName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
