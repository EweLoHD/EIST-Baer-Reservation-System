package eist.eistbaer.reservationsystem.reservation;

import eist.eistbaer.reservationsystem.reservation.email.EmailUtils;
import eist.eistbaer.reservationsystem.reservation.util.CancellationException;
import eist.eistbaer.reservationsystem.reservation.util.InvalidReservationException;
import eist.eistbaer.reservationsystem.restaurant.RestaurantRepository;
//import eist.eistbaer.reservationsystem.restaurant.type.RestaurantTypeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@CrossOrigin(origins="*")
public class ReservationController {
    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/reservations/{id}")
    Reservation getReservationById(@PathVariable String id) {
        return reservationRepository.findById(id).orElseThrow();
    }

    @PostMapping("/reservations")
    Reservation addReservation(@RequestBody Reservation reservation) {
        if (reservation.isValid()) {
            LocalDateTime dateTime = reservation.getFromTime().atDate(reservation.getDate());

            if (reservation.getFromTime().isAfter(LocalTime.of(0, 0)) && reservation.getFromTime().isBefore(LocalTime.of(4, 0))) {
                dateTime = dateTime.plusDays(1);
            }

            // Reservations that are made within 12h before Start are automatically confirmed.
            if (dateTime.isBefore(LocalDateTime.now().plusHours(12))) {
                reservation.setConfirmed(true);
                reservation.setConfirmationMailSent(true);
            }

            Reservation newReservation = reservationRepository.save(reservation);

            EmailUtils.defaultEmailUtils().sendCompletedMail(newReservation);

            return newReservation;
        } else {
            throw new InvalidReservationException();
        }
    }

    @PostMapping("/reservations/{id}/confirm")
    Reservation confirmReservation(@PathVariable String id) {
        return reservationRepository.findById(id).map(r -> {
            if (r.isConfirmationMailSent()) {
                r.setConfirmed(true);
                return reservationRepository.save(r);
            } else {
                throw new RuntimeException("Reservation can't be confirmed, because E-Mail hasn't been sent yet!");
            }
        }).orElseThrow();
    }

    @DeleteMapping("/reservations/{id}")
    void deleteReservation(@PathVariable String id) {
        if (!reservationRepository.findById(id).orElseThrow().isConfirmed()) {
            reservationRepository.deleteById(id);
        } else {
            throw new CancellationException();
        }
    }

}
