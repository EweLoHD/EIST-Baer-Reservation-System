package eist.eistbaer.reservationsystem.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Invalid Reservation")
public class InvalidReservationException extends RuntimeException {

    public InvalidReservationException() {
        super("Invalid Reservation");
    }
}
