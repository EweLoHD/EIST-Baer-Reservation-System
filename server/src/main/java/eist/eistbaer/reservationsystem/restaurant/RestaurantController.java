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
        return RestaurantSearchUtility.search(searchQuery, restaurantRepository, restaurantTypeRepository);
    }
}
