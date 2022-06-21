package eist.eistbaer.reservationsystem.restaurant;

import eist.eistbaer.reservationsystem.restaurant.type.RestaurantType;
import eist.eistbaer.reservationsystem.restaurant.type.RestaurantTypeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantTypeRepository restaurantTypeRepository;
    private final double MIN_RESTAURANT_TYPE = 0.7;
    private final double MIN_RESTAURANT_NAME = 0.1;

    public RestaurantController(RestaurantRepository restaurantRepository, RestaurantTypeRepository restaurantTypeRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantTypeRepository = restaurantTypeRepository;
    }

    @GetMapping("/restaurants")
    List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @GetMapping("/restaurants/{id}")
    Restaurant getRestaurantById(@PathVariable Long id) {
        // TODO Add Error Handling
        return restaurantRepository.findById(id).orElseThrow();
    }

    @PostMapping("/restaurants")
    Restaurant newRestaurant(@RequestBody Restaurant restaurant) {
        RestaurantType type = restaurantTypeRepository.findByName(restaurant.getRestaurantType().getName());
        if (type != null) {
            restaurant.setRestaurantType(type);
        }

        return restaurantRepository.save(restaurant);
    }

    @GetMapping("/search")
    List<Restaurant> searchRestaurant(@RequestParam String searchQuery) {
        List<RestaurantType> restaurantType = restaurantTypeRepository.findAll();
        List<Restaurant> restaurants = getAllRestaurants();

        List<Restaurant> result = new ArrayList<>();

        boolean searchOfType = false;
        String searchType = null;
        for (RestaurantType type : restaurantType) {
            if (similarity(type.getName(), searchQuery) > MIN_RESTAURANT_TYPE) {
                searchOfType = true;
                searchType = type.getName();
                break;
            }
        }
        if (searchOfType) {
            for (Restaurant restaurant : restaurants) {
                if(restaurant.getRestaurantType().getName().equals(searchType)) {
                    result.add(restaurant);
                }
            }

        } else {
            for (Restaurant restaurant : restaurants) {
                if (similarity(searchQuery, restaurant.getName()) > MIN_RESTAURANT_NAME) {
                    result.add(restaurant);
                }
            }
            result = result.stream().sorted(Comparator.comparing(r -> similarity(searchQuery, ((Restaurant) r).getName())).reversed()).collect(Collectors.toList());
        }
        return result;
    }

    private static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) {
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0;}
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
