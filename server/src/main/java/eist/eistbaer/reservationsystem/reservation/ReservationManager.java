package eist.eistbaer.reservationsystem.reservation;

import eist.eistbaer.reservationsystem.reservation.email.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class ReservationManager {

    private static final Logger log = LoggerFactory.getLogger(ReservationManager.class);

    @Autowired
    private ReservationRepository reservationRepository;

    /**
     * Delete Reservations that are already over.
     * This Method gets executed automatically on startup and every day at 4:00
     */
    @PostConstruct // Run on Startup
    @Scheduled(cron = "0 0 4 * * ?") // Run every Day at 4am
    public void deletePastReservations() {
        List<Reservation> pastReservations = reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getDate().isBefore(LocalDate.now()))
                .toList();

        log.info("Deleting " + pastReservations.size() + " passed Reservations");

        reservationRepository.deleteAll(pastReservations);
    }

    /**
     * Send Confirmation Mails 24h or less before the start of the Reservation
     * This Method gets executed automatically every Minute.
     */
    @PostConstruct // Run on Startup
    @Scheduled(fixedRate = 1000 * 60) // Run every Minute
    public void sendConfirmationMails() {
        List<Reservation> reservations = reservationRepository.findAll().stream()
                .filter(reservation -> !reservation.isConfirmationMailSent())
                .filter(reservation -> reservation.getFromTime().atDate(reservation.getDate()).minusDays(1).isBefore(LocalDateTime.now()))
                .toList();

        if (!reservations.isEmpty()) log.info("Sending " + reservations.size() + " Confirmation E-Mails");

        reservations.forEach(reservation -> {
            EmailUtils.defaultEmailUtils().sendConfirmationMail(reservation);

            reservationRepository.findById(reservation.getId())
                    .map(r -> {
                        r.setConfirmationMailSent(true);
                        return reservationRepository.save(r);
                    });
        });
    }

    /**
     * Delete Reservations that have not been confirmed 12h before the start of the reservation
     * This Method gets executed automatically every Minute.
     */
    @PostConstruct // Run on Startup
    @Scheduled(fixedRate = 1000 * 60) // Run every Minute
    public void deleteUnconfirmedReservations() {
        List<Reservation> unconfirmedReservations = reservationRepository.findAll().stream()
                .filter(r -> !r.isConfirmed())
                .filter(r -> {
                    LocalDateTime dateTime = r.getFromTime().atDate(r.getDate());

                    if (r.getFromTime().isAfter(LocalTime.of(0, 0)) && r.getFromTime().isBefore(LocalTime.of(4, 0))) {
                        dateTime = dateTime.plusDays(1);
                    }

                    return dateTime.minusHours(12).isBefore(LocalDateTime.now());
                })
                .toList();

        if (unconfirmedReservations.size() > 0) log.info("Deleting " + unconfirmedReservations.size() + " unconfirmed Reservations");

        reservationRepository.deleteAll(unconfirmedReservations);
    }


}
