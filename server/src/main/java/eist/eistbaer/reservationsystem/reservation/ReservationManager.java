package eist.eistbaer.reservationsystem.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;

@Component
public class ReservationManager {

    private static final Logger log = LoggerFactory.getLogger(ReservationManager.class);

    @Autowired
    private ReservationRepository reservationRepository;

    @PostConstruct // Run on Startup
    @Scheduled(cron = "0 0 4 * * ?") // Run every Day at 4am
    public void deletePastReservations() {
        List<Reservation> pastReservations = reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getDate().isBefore(LocalDate.now()))
                        .toList();

        log.info("Deleting " + pastReservations.size() + " passed Reservations");

        reservationRepository.deleteAll(pastReservations);
    }


}
