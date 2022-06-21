package eist.eistbaer.reservationsystem.restaurant.openingtime;

import eist.eistbaer.reservationsystem.restaurant.RestaurantRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OpeningTimeController {
    private final RestaurantRepository restaurantRepository;

    public OpeningTimeController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/restaurants/times/{id}")
    List<OpeningTime> getAllTypes(@PathVariable Long id) {
        return restaurantRepository.findById(id).orElseThrow().getOpeningTimes();
    }

//    @PostMapping("/restaurants/types")
//    RestaurantType newRestaurantType(@RequestBody RestaurantType restaurantType) {
//        return restaurantRepository.save(restaurantType);
//    }
}
