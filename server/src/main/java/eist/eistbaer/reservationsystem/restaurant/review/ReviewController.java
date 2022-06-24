package eist.eistbaer.reservationsystem.restaurant.review;

import eist.eistbaer.reservationsystem.restaurant.Restaurant;
import eist.eistbaer.reservationsystem.restaurant.RestaurantRepository;
import eist.eistbaer.reservationsystem.restaurant.openingtime.OpeningTime;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {
    private final RestaurantRepository restaurantRepository;
    private final ReviewRepository reviewRepository;

    public ReviewController(RestaurantRepository restaurantRepository, ReviewRepository reviewRepository) {
        this.restaurantRepository = restaurantRepository;
        this.reviewRepository = reviewRepository;
    }

    @GetMapping("/restaurants/reviews/{id}")
    List<Review> getAllReviews(@PathVariable Long id) {
        return restaurantRepository.findById(id).orElseThrow().getReviews();
    }

    @PostMapping("/restaurants/reviews/{id}")
    @ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "To show an example of a custom message")
    Review addReview(@PathVariable Long id, @RequestBody Review review) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();
        if (restaurant != null){
//            List<Review> reviews = restaurant.getReviews();
//            reviews.add(review);
//            restaurant.setReviews(reviews);
            return reviewRepository.save(review);
        }
        throw new ForbiddenException();
    }
}