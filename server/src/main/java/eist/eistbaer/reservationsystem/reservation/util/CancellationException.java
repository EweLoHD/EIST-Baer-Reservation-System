package eist.eistbaer.reservationsystem.reservation.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Reservation could not be canceled, because it has already been confirmed.")
public class CancellationException extends RuntimeException {

    public CancellationException() {
        super("Reservation could not be canceled, because it has already been confirmed.");
    }

}
