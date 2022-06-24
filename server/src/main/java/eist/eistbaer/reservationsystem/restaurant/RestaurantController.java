package eist.eistbaer.reservationsystem.restaurant;

import eist.eistbaer.reservationsystem.reservation.ReservationRepository;
import eist.eistbaer.reservationsystem.restaurant.type.RestaurantType;
import eist.eistbaer.reservationsystem.restaurant.type.RestaurantTypeRepository;
import eist.eistbaer.reservationsystem.restaurantSearchUtiliy.RestaurantSearchUtility;
import eist.eistbaer.reservationsystem.restaurantSearchUtiliy.SearchBodyReply;
import eist.eistbaer.reservationsystem.restaurantSearchUtiliy.SearchBodyRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="*")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantTypeRepository restaurantTypeRepository;
    private final ReservationRepository reservationRepository;


    public RestaurantController(RestaurantRepository restaurantRepository, RestaurantTypeRepository restaurantTypeRepository, ReservationRepository reservationRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantTypeRepository = restaurantTypeRepository;
        this.reservationRepository = reservationRepository;
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
    SearchBodyReply searchRestaurant(@RequestBody SearchBodyRequest searchBodyRequest) {
        return RestaurantSearchUtility.search(searchBodyRequest, restaurantRepository, restaurantTypeRepository, reservationRepository);
    }
}
