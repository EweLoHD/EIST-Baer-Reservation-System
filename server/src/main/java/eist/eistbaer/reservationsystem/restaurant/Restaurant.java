package eist.eistbaer.reservationsystem.restaurant;

import eist.eistbaer.reservationsystem.restaurant.pictures.RestaurantPicture;
import eist.eistbaer.reservationsystem.restaurant.priceCategory.PriceCategory;
import eist.eistbaer.reservationsystem.restaurant.priceCategory.PriceCategoryConverter;
import eist.eistbaer.reservationsystem.restaurant.type.RestaurantType;

import javax.persistence.*;
import java.util.List;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private String description;
    private String websiteLink;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="restaurant_id")
    private List<RestaurantPicture> restaurantPictures;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private RestaurantType restaurantType;
    @Convert(converter = PriceCategoryConverter.class)
    private PriceCategory priceCategory;

    public Restaurant() {
    }

    public Restaurant(String name, String description, String websiteLink, List<RestaurantPicture> restaurantPictures, RestaurantType restaurantType, PriceCategory priceCategory) {
        this.name = name;
        this.description = description;
        this.websiteLink = websiteLink;
        this.restaurantPictures = restaurantPictures;
        this.restaurantType = restaurantType;
        this.priceCategory = priceCategory;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public List<RestaurantPicture> getRestaurantPictures() {
        return restaurantPictures;
    }

    public void setRestaurantPictures(List<RestaurantPicture> restaurantPictures) {
        this.restaurantPictures = restaurantPictures;
    }

    public RestaurantType getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(RestaurantType restaurantType) {
        this.restaurantType = restaurantType;
    }

    public PriceCategory getPriceCategory() {
        return priceCategory;
    }

    public void setPriceCategory(PriceCategory priceCategory) {
        this.priceCategory = priceCategory;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", websiteLink='" + websiteLink + '\'' +
                ", restaurantPictures=" + restaurantPictures +
                ", restaurantType=" + restaurantType +
                ", priceCategory=" + priceCategory +
                '}';
    }
}
