package eist.eistbaer.reservationsystem.restaurant.type;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins="*")
public class RestaurantTypeController {

    private final RestaurantTypeRepository restaurantTypeRepository;

    public RestaurantTypeController(RestaurantTypeRepository restaurantTypeRepository) {
        this.restaurantTypeRepository = restaurantTypeRepository;
    }

    @GetMapping("/restaurants/types")
    List<String> getAllTypes() {
        return restaurantTypeRepository.findAll().stream().map(RestaurantType::getName).distinct().collect(Collectors.toList());
    }

    @PostMapping("/restaurants/types")
    RestaurantType newRestaurantType(@RequestBody RestaurantType restaurantType) {
        return restaurantTypeRepository.save(restaurantType);
    }
}
