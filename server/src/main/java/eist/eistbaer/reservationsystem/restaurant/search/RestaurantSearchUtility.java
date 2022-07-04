package eist.eistbaer.reservationsystem.restaurant.search;

import eist.eistbaer.reservationsystem.reservation.Reservation;
import eist.eistbaer.reservationsystem.reservation.ReservationRepository;
import eist.eistbaer.reservationsystem.restaurant.Restaurant;
import eist.eistbaer.reservationsystem.restaurant.RestaurantRepository;
import eist.eistbaer.reservationsystem.restaurant.address.Address;
import eist.eistbaer.reservationsystem.restaurant.openingtime.OpeningTime;
import eist.eistbaer.reservationsystem.restaurant.priceCategory.PriceCategory;
import eist.eistbaer.reservationsystem.restaurant.reviewrating.ReviewRating;
import eist.eistbaer.reservationsystem.restaurant.table.RestaurantTable;
import eist.eistbaer.reservationsystem.restaurant.type.RestaurantType;
import eist.eistbaer.reservationsystem.restaurant.type.RestaurantTypeRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantSearchUtility {
    private static final double MIN_RESTAURANT_TYPE = 0.7;
    private static final double MIN_RESTAURANT_NAME = 0.1;

    public static SearchResponse search(SearchRequestBody searchBodyRequest, RestaurantRepository restaurantRepository, RestaurantTypeRepository restaurantTypeRepository, ReservationRepository reservationRepository) {
        List<RestaurantType> restaurantTypes = restaurantTypeRepository.findAll();
        List<Restaurant> restaurants = restaurantRepository.findAll();

        SearchResponse reply = new SearchResponse();

        String searchQuery = searchBodyRequest.getQuery();
        RestaurantType searchedType = null;

        if (searchQuery != null) {
            searchedType = checkIfSearchAfterRestaurantType(restaurantTypes, searchQuery);
        }

        if (searchedType == null && searchQuery != null) {
            restaurants = searchAfterName(searchQuery, restaurants);
            reply.setQuery(searchQuery);
        }

        if (searchBodyRequest.getRestaurantType() != null) {
            restaurants = searchAfterType(searchBodyRequest.getRestaurantType(), restaurants);
            reply.setRestaurantType(searchBodyRequest.getRestaurantType());
        } else if (searchedType != null) {
            restaurants = searchAfterType(searchedType, restaurants);
            reply.setRestaurantType(searchedType);
            reply.setQuery(searchQuery);
        }


        if (searchBodyRequest.getRating() != null) {
            restaurants = filterAfterRating(searchBodyRequest.getRating(), restaurants);
            reply.setRating(searchBodyRequest.getRating());
        }
        if (searchBodyRequest.getPrice() != null) {
            restaurants = filterAfterPrice(searchBodyRequest.getPrice(), restaurants);
            reply.setPrice(searchBodyRequest.getPrice());
        }
        if(searchBodyRequest.getLocation() != null && searchBodyRequest.getDistance() != 0) {
            restaurants = filterAfterDistance(searchBodyRequest.getLocation(), searchBodyRequest.getDistance(), restaurants);
            reply.setLocation(searchBodyRequest.getLocation());
            reply.setDistance(searchBodyRequest.getDistance());
        }
        if (searchBodyRequest.getDate() != null && searchBodyRequest.getTime() != null && searchBodyRequest.getPeople() != 0) {
            restaurants = filterAfterDateTimePeople(searchBodyRequest.getDate(), searchBodyRequest.getTime(), searchBodyRequest.getPeople(), restaurants);
            reply.setDate(searchBodyRequest.getDate());
            reply.setTime(searchBodyRequest.getTime());
            reply.setPeople(searchBodyRequest.getPeople());
        } else if (searchBodyRequest.getDate() != null && searchBodyRequest.getTime() != null) {
            restaurants = filterAfterDateAndTime(searchBodyRequest.getDate(), searchBodyRequest.getTime(), restaurants);
            reply.setDate(searchBodyRequest.getDate());
            reply.setTime(searchBodyRequest.getTime());
        } else if (searchBodyRequest.getDate() != null) {
            restaurants = filterAfterDate(searchBodyRequest.getDate(), restaurants);
            reply.setDate(searchBodyRequest.getDate());
        }

        reply.setRestaurants(restaurants);
        return reply;
    }

    private static RestaurantType checkIfSearchAfterRestaurantType(List<RestaurantType> restaurantTypes, String searchQuery) {
        for (RestaurantType type : restaurantTypes) {
            if (similarity(type.getName(), searchQuery) > MIN_RESTAURANT_TYPE) {
                return type;
            }
        }
        return null;
    }

    private static List<Restaurant> searchAfterType(RestaurantType restaurantType, List<Restaurant> restaurants) {
        List<Restaurant> result = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            if (restaurant.getRestaurantType().getId().equals(restaurantType.getId())) {
                result.add(restaurant);
            }
        }
        return result;
    }

    private static List<Restaurant> searchAfterName(String searchQuery, List<Restaurant> restaurants) {
        List<Restaurant> result = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            if (similarity(searchQuery, restaurant.getName()) > MIN_RESTAURANT_NAME) {
                result.add(restaurant);
            }
        }
        result = result.stream().sorted(Comparator.comparing(r -> similarity(searchQuery, ((Restaurant) r).getName())).reversed()).collect(Collectors.toList());
        return result;
    }

    private static List<Restaurant> filterAfterRating(ReviewRating minRating, List<Restaurant> restaurants) {
        return restaurants.stream().filter(r -> r.getAverageRating().num() >= minRating.num()).toList();
    }

    private static List<Restaurant> filterAfterPrice(PriceCategory maxPriceCategory, List<Restaurant> restaurants) {
        return restaurants.stream().filter(r -> r.getPriceCategory().num() <= maxPriceCategory.num()).toList();
    }

    private static List<Restaurant> filterAfterDistance(Address location, int maxDistance, List<Restaurant> restaurants) {
        return restaurants.stream().filter(r -> r.getAddress().calculateDistance(location) <= maxDistance).toList();
    }

    private static List<Restaurant> filterAfterDate(LocalDate date, List<Restaurant> restaurants) {
        DayOfWeek searchedDayOfWeek = date.getDayOfWeek();
        List<Restaurant> result = new ArrayList<>();
        for (Restaurant restaurant : restaurants) {
            List<OpeningTime> openingTimesRestaurant = restaurant.getOpeningTimes();
            for (OpeningTime openingTime : openingTimesRestaurant) {
                if (openingTime.getDayOfWeek().equals(searchedDayOfWeek)) {
                    result.add(restaurant);
                    break;
                }
            }
        }
        return result;
    }


    private static List<Restaurant> filterAfterDateAndTime(LocalDate date, LocalTime time, List<Restaurant> restaurants) {
        List<Restaurant> result = new ArrayList<>();
        restaurants = filterAfterDate(date, restaurants);
        for (Restaurant restaurant : restaurants) {
            if (!OpenForDateAndTime(date, time, restaurant)) {
                continue;
            }
            List<RestaurantTable> tables = restaurant.getRestaurantTables();
            List<RestaurantTable> freeTables = new ArrayList<>();
            for (RestaurantTable table : tables) {
                if (table.isFreeBetween(date, time, time.plusHours(Reservation.DEFAULT_DURATION))) {
                    freeTables.add(table);
                }
            }
            if (!freeTables.isEmpty()) {
                result.add(restaurant);
            }
        }
        return result;
    }

    private static List<Restaurant> filterAfterDateTimePeople(LocalDate date, LocalTime time, int minPeople, List<Restaurant> restaurants) {
        List<Restaurant> result = new ArrayList<>();
        restaurants = filterAfterDate(date, restaurants);
        for (Restaurant restaurant : restaurants) {
            if (!OpenForDateAndTime(date, time, restaurant)) {
                continue;
            }
            List<RestaurantTable> tables = restaurant.getRestaurantTables();
            List<RestaurantTable> freeTables = new ArrayList<>();
            for (RestaurantTable table : tables) {
                if (table.isFreeBetween(date, time, time.plusHours(Reservation.DEFAULT_DURATION)) && table.getCapacity() >= minPeople) {
                    freeTables.add(table);
                }
            }
            if (!freeTables.isEmpty()) {
                result.add(restaurant);
            }
        }
        return result;
    }

    private static boolean OpenForDateAndTime(LocalDate date, LocalTime time, Restaurant restaurant) {
        DayOfWeek searchedDayOfWeek = date.getDayOfWeek();
        List<OpeningTime> openingTimesRestaurant = restaurant.getOpeningTimes();
        LocalTime toTime = time.plusHours(Reservation.DEFAULT_DURATION);
        LocalDateTime wantedFromTime = time.atDate(LocalDate.now());
        LocalDateTime wantedToTime = toTime.atDate(time.isBefore(toTime) ? LocalDate.now() : LocalDate.now().plusDays(1));

        for (OpeningTime openingTime : openingTimesRestaurant) {
            LocalDateTime openingFromTime = openingTime.getFromTime().atDate(LocalDate.now());
            LocalDateTime openingToTime = openingTime.getToTime().atDate(openingTime.getFromTime().isBefore(openingTime.getToTime()) ? LocalDate.now() : LocalDate.now().plusDays(1));
            if (openingTime.getDayOfWeek().equals(searchedDayOfWeek) && (openingFromTime.isBefore(wantedFromTime) || openingFromTime.equals(wantedFromTime)) && (openingToTime.isAfter(wantedToTime) || openingToTime.equals(wantedToTime))) {
                return true;
            }
        }
        return false;
    }
    private static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2;
            shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) {
            return 1.0;
        }
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    private static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }
}
