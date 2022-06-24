package eist.eistbaer.reservationsystem.restaurant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eist.eistbaer.reservationsystem.restaurant.openingtime.OpeningTime;
import eist.eistbaer.reservationsystem.restaurant.pictures.RestaurantPicture;
import eist.eistbaer.reservationsystem.restaurant.priceCategory.PriceCategory;
import eist.eistbaer.reservationsystem.restaurant.priceCategory.PriceCategoryConverter;
import eist.eistbaer.reservationsystem.restaurant.review.Review;
import eist.eistbaer.reservationsystem.restaurant.reviewrating.ReviewRating;
import eist.eistbaer.reservationsystem.restaurant.type.RestaurantType;
import eist.eistbaer.reservationsystem.restaurant.address.Address;
import eist.eistbaer.reservationsystem.restaurant.table.RestaurantTable;

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
    @ManyToOne(cascade = CascadeType.PERSIST)
    private RestaurantType restaurantType;
    @Convert(converter = PriceCategoryConverter.class)
    private PriceCategory priceCategory;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="restaurant_id")
    @JsonIgnoreProperties("id")
    private List<OpeningTime> openingTimes;

    @OneToOne
    @JsonIgnoreProperties("id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="restaurant_id")
    private List<RestaurantTable> restaurantTables;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="restaurant_id")
    private List<Review> reviews;

    public List<RestaurantTable> getRestaurantTables() {
        return restaurantTables;
    }

    public void setRestaurantTables(List<RestaurantTable> restaurantTables) {
        this.restaurantTables = restaurantTables;
    }

    public Restaurant() {
    }

//    public Restaurant(String name, String description, String websiteLink, List<RestaurantPicture> restaurantPictures, RestaurantType restaurantType, PriceCategory priceCategory, List<OpeningTime> openingTimes, Address address) {
//        this.name = name;
//        this.description = description;
//        this.websiteLink = websiteLink;
//        this.restaurantPictures = restaurantPictures;
//        this.restaurantType = restaurantType;
//        this.priceCategory = priceCategory;
//        this.openingTimes = openingTimes;
//    }

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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<OpeningTime> getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(List<OpeningTime> openingTimes) {
        this.openingTimes = openingTimes;
    }

    public List<RestaurantTable> getTables() {
        return restaurantTables;
    }

    public void setTables(List<RestaurantTable> restaurantTables) {
        this.restaurantTables = restaurantTables;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public ReviewRating getAverageRating() {
        double sumRating = 0.0;
        double numberOfReviews = 0.0;
        for(Review r : reviews) {
            sumRating += r.getRating().num();
            numberOfReviews++;
        }
        int averageRating = (int) Math.round(sumRating / numberOfReviews);
        return ReviewRating.of(averageRating);
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
                ", openingTimes=" + openingTimes +
                ", address=" + address +
                ", tables=" + restaurantTables +
                ", reviews=" + reviews +
                '}';
    }


}
