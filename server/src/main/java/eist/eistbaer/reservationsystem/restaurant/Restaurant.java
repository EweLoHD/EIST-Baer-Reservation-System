package eist.eistbaer.reservationsystem.restaurant;

import com.fasterxml.jackson.annotation.*;
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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
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

    @OneToOne(cascade = {CascadeType.ALL})
    @JsonIgnoreProperties("id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="restaurant_id")
    private List<RestaurantTable> restaurantTables;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="restaurant_id")
    private List<Review> reviews;

    public Restaurant() {
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

    public List<RestaurantTable> getRestaurantTables() {
        return restaurantTables;
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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @JsonGetter
    public double getRating() {
        return reviews.stream().mapToInt(r -> r.getRating().num()).average().orElse(0);
    }

    @JsonIgnore
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

    public List<OpeningTime> getOpeningTimesOfDay(DayOfWeek dayOfWeek) {
        return openingTimes.stream()
                .filter(openingTime -> openingTime.getDayOfWeek().getValue() == dayOfWeek.getValue())
                .toList();
    }

    /**
     * Checks if the given Timestamp (from - to) lies in the Opening Times of the Restaurant at the given Date
     * @param date Date
     * @param fromTime Start time to the Timestamp
     * @param toTime End time to the Timestamp
     * @return true if (fromTime - toTime) lies in the Opening Times of the Restaurant at the given Date
     */
    public boolean hasOpened(LocalDate date, LocalTime fromTime, LocalTime toTime) {
        LocalDateTime from = fromTime.atDate(fromTime.isAfter(LocalTime.of(4, 0)) ? LocalDate.now() : LocalDate.now().plusDays(1));
        LocalDateTime to = toTime.atDate(fromTime.isBefore(toTime) ? LocalDate.now() : LocalDate.now().plusDays(1));

        List<OpeningTime> openingTimes = getOpeningTimesOfDay(date.getDayOfWeek());

        for (OpeningTime o : openingTimes) {
            LocalDateTime oF = o.getFromTime().atDate(LocalDate.now());
            LocalDateTime oT = o.getToTime().atDate(o.getFromTime().isBefore(o.getToTime()) ? LocalDate.now() : LocalDate.now().plusDays(1));

            if ((oF.isBefore(from) || oF.equals(from))
                    && (oT.isAfter(to) || oT.equals(to))) {
                return true;
            }
        }

        return false;
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
                ", restaurantTables=" + restaurantTables +
                ", reviews=" + reviews +
                '}';
    }
}
