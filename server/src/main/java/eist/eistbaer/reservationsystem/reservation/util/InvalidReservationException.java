package eist.eistbaer.reservationsystem.reservation.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Reservation")
public class InvalidReservationException extends RuntimeException {

    public InvalidReservationException() {
        super("Invalid Reservation");
    }
}
