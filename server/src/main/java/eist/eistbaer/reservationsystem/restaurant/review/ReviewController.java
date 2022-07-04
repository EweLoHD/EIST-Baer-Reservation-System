package eist.eistbaer.reservationsystem.restaurant.review;

import eist.eistbaer.reservationsystem.restaurant.Restaurant;
import eist.eistbaer.reservationsystem.restaurant.RestaurantRepository;
import eist.eistbaer.reservationsystem.restaurant.openingtime.OpeningTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="*")
public class ReviewController {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    public ReviewController(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository) {
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/restaurants/{id}/reviews")
    List<Review> getAllReviews(@PathVariable Long id) {
        return restaurantRepository.findById(id).orElseThrow().getReviews();
    }

    /*@PostMapping("/restaurants/{id}/reviews")
    Review addReview(@PathVariable Long id, @RequestBody Review review) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();

        return reviewRepository.save(review);
    }*/
}
