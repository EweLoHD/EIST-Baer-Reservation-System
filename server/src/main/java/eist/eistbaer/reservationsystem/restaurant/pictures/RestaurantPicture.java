package eist.eistbaer.reservationsystem.restaurant.pictures;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class RestaurantPicture {

    @Id
    @GeneratedValue
    private Long id;

    @JsonValue
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
