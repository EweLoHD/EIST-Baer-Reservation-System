package eist.eistbaer.reservationsystem.reservation;

import eist.eistbaer.reservationsystem.reservation.email.EmailUtils;
import eist.eistbaer.reservationsystem.restaurant.Restaurant;
import eist.eistbaer.reservationsystem.restaurant.RestaurantRepository;
import eist.eistbaer.reservationsystem.restaurant.type.RestaurantType;
//import eist.eistbaer.reservationsystem.restaurant.type.RestaurantTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/reservations/email/confirmation/{id}")
    ResponseEntity<Reservation> sendConfirmationEmail(@PathVariable Long id) {
        // TODO Add Error Handling
        Reservation currentReservation = reservationRepository.findById(id).orElseThrow();
        if(currentReservation != null){
            EmailUtils currentEmailUtils = new EmailUtils("smtp.web.de", 587, "eist_table_booking", "EistBaer2022!");
            try {
                currentEmailUtils.sendMail(true, currentReservation);
                //Response, wenn die Email erfolgreich gesendet werden konnte
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/reservations/email/cancellation/{id}")
    ResponseEntity<Reservation> sendCancellationRequest(@PathVariable Long id) {
        // TODO Add Error Handling
        Reservation currentReservation = reservationRepository.findById(id).orElseThrow();
        if(currentReservation != null){
            //TODO: falls benötigt noch den Versand einer Cancellation-Bestätigung implementieren
            //EmailUtils currentEmailUtils = new EmailUtils("smtp.web.de", 587, "eist_table_booking", "EistBaer2022!");
            /*try {
                currentEmailUtils.sendMail(true, currentReservation);
                //Response, wenn die Email erfolgreich gesendet werden konnte
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }*/
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
