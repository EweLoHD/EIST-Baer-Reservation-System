package eist.eistbaer.reservationsystem.reservation.calendar;


import java.util.GregorianCalendar;


import eist.eistbaer.reservationsystem.reservation.Reservation;
import eist.eistbaer.reservationsystem.reservation.ReservationController;
import eist.eistbaer.reservationsystem.reservation.ReservationRepository;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Duration;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;


@RestController
public class CalenderController {
    private final ReservationRepository reservationRepository;

    public CalenderController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @GetMapping(path = "/generate-calendar/{id}")
    public ResponseEntity generateCalenderFile(@PathVariable String id) {
        Reservation currentReservation = reservationRepository.findById(id).orElseThrow();
        Calendar cal = new Calendar();

        try {
            TimeZone tz = Utils.getTimezone("Europe/Berlin");

          /* Make up a start date 10 days from now.
             Ensure on an hour boundary
           */
            java.util.Calendar startDate = new GregorianCalendar();
            startDate.setTimeZone(tz);
            // startDate.add(java.util.Calendar.DAY_OF_MONTH, 0);
            startDate.set(currentReservation.getDate().getYear(), currentReservation.getDate().getMonthValue(), currentReservation.getDate().getDayOfYear(), currentReservation.getFromTime().getHour(), currentReservation.getFromTime().getMinute());
            // startDate.set(java.util.Calendar.MINUTE, 0);
            // startDate.set(java.util.Calendar.SECOND, 0);

            // Create the event
            String eventName = "Reservierung bei " + currentReservation.getRestaurant().getName();
            DateTime start = new DateTime(startDate.getTime());
            start.setTimeZone(tz);

            VEvent event = new VEvent(start, eventName);

            PropertyList props = event.getProperties();

            /* One hour */
            Duration dur = new Duration(null, "PT1H");
            props.add(dur);

            /* Need a uid */
            props.add(Utils.generateUid());

            /* Add the location */
            props.add(new Location("https://www.google.com/maps?q=" + currentReservation.getRestaurant().getAddress().getLat() + "," + currentReservation.getRestaurant().getAddress().getLon()));

      /* And the description - Note that ical4j handles any
         wrapping of long lines and with the
         escaping of special characters.
       */
            props.add(new Description(
                    "Danke für die Reservierung in unserem Restaurant wir freuen uns auf Sie "));

            /* Create a calendar object */
            PropertyList calProps = cal.getProperties();

            calProps.add(
                    new ProdId("-//ABC Corporation/NONSGML iCal4j 1.0//EN"));
            calProps.add(CalScale.GREGORIAN);
            calProps.add(Version.VERSION_2_0);

            // Add the event and print
            cal.getComponents().add(event);

            Utils.pline("Example " + "test");
            Utils.pline("");

      /* Use the ical4j CalendarOutputter class to fold the output lines
         to a maximum length.
       */
            Utils.pline(Utils.calToString(cal));
        } catch (Throwable t) {
            t.printStackTrace();
        }
        byte[] calendarByte = cal.toString().getBytes();
        Resource resource = new ByteArrayResource(calendarByte);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=mycalendar.ics");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        //return new ResponseEntity(HttpStatus.OK);

        return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);

    }

}

