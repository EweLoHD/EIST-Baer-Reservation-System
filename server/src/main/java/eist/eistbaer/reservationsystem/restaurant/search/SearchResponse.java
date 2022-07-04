package eist.eistbaer.reservationsystem.restaurant.search;

import eist.eistbaer.reservationsystem.restaurant.Restaurant;
import eist.eistbaer.reservationsystem.restaurant.address.Address;
import eist.eistbaer.reservationsystem.restaurant.priceCategory.PriceCategory;
import eist.eistbaer.reservationsystem.restaurant.reviewrating.ReviewRating;
import eist.eistbaer.reservationsystem.restaurant.type.RestaurantType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class SearchResponse {
    private List<Restaurant> restaurants;
    private String query;
    private LocalDate date;
    private LocalTime time;
    private int people;
    private Address location;
    private int distance;
    private ReviewRating rating;
    private PriceCategory price;
    private RestaurantType restaurantType;

    public SearchResponse() {
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public ReviewRating getRating() {
        return rating;
    }

    public void setRating(ReviewRating rating) {
        this.rating = rating;
    }

    public PriceCategory getPrice() {
        return price;
    }

    public void setPrice(PriceCategory price) {
        this.price = price;
    }

    public RestaurantType getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(RestaurantType restaurantType) {
        this.restaurantType = restaurantType;
    }
}
