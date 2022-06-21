package eist.eistbaer.reservationsystem.reservation;

import eist.eistbaer.reservationsystem.restaurant.Restaurant;
import eist.eistbaer.reservationsystem.restaurant.RestaurantRepository;
import eist.eistbaer.reservationsystem.restaurant.type.RestaurantType;
//import eist.eistbaer.reservationsystem.restaurant.type.RestaurantTypeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {
    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;

    public ReservationController(RestaurantRepository restaurantRepository, ReservationRepository reservationRepository) {
        this.restaurantRepository = restaurantRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/reservations")
    List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @GetMapping("/reservations/{id}")
    Reservation getReservationById(@PathVariable Long id) {
        // TODO Add Error Handling
        return reservationRepository.findById(id).orElseThrow();
    }

    @PostMapping("/reservations")
    Reservation addReservation(@RequestBody Reservation reservation) {
        // TODO: Add Error Handling
        return reservationRepository.save(reservation);
    }
}
