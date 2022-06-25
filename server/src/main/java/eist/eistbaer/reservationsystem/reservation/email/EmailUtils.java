package eist.eistbaer.reservationsystem.reservation.email;

import eist.eistbaer.reservationsystem.reservation.Reservation;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

public class EmailUtils {

    //attributes
    private String username;
    private String password;

    //attributes for mailcontent

    //TODO A little cleanup (;

    private String restaurantName;
    private String customerName;
    private java.util.Date reservationTime;
    private String cancellationLink;
    private String confirmationLink;

    private final Properties prop;

    //constructors
    public EmailUtils(String host, int port, String username, String password) {
        prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.ssl.trust", host);

        this.username = username;
        this.password = password;
    }

    public EmailUtils(String host, int port) {
        prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
    }

    public static EmailUtils defaultEmailUtils() {
        //TODO Load Credentials from Config-File
        return new EmailUtils("smtp.web.de", 587, "eist_table_booking", "EistBaer2022!");
    }

    //methods

    public String getCancellationLink() {
        return cancellationLink;
    }

    public void setCancellationLink(String cancellationLink) {
        this.cancellationLink = cancellationLink;
    }

    public String getConfirmationLink() {
        return confirmationLink;
    }

    public void setConfirmationLink(String confirmationLink) {
        this.confirmationLink = confirmationLink;
    }

    /**
     * E-Mail, that gets sent right after a new Reservation is created
     * @param reservation The given Reservation
     * @throws MessagingException In case of Error regarding the E-Mail Service
     */
    public void sendCompletedMail(Reservation reservation) throws MessagingException {
        this.sendMail(true, reservation);
    }

    /**
     * E-Mail, that gets sent 24h before the Reservation
     * @param reservation The given Reservation
     * @throws MessagingException In case of Error regarding the E-Mail Service
     */
    public void sendConfirmationMail(Reservation reservation) throws MessagingException {
        this.sendMail(false, reservation);
    }

    /*
        sendet eine Mail mittels des hinterlegten Postfaches
        -> falls cancellationRequest = true, dann wird die erste Mailvorlage mit Link zum Wiederruf versendet
        -> falls cancellationRequest = false, dann wird die zweite Mailvorlage mit Link zum Bestätigen in den letzten 24h vor der Reservierung versendet
         */
    public void sendMail(boolean cancellationRequest, Reservation reservation) throws MessagingException {

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress("eist_table_booking@web.de"));
        message.setRecipients(
                //Message.RecipientType.TO, InternetAddress.parse("eist_table_booking@web.de"));
                //Für einen Email-Versand an den Kunden
                Message.RecipientType.TO, InternetAddress.parse(reservation.getClientEmail()));

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        //Erste Emailvorlage mit Cancellationlink
        if (cancellationRequest) {
            //message.setSubject("1st Reservation Notice - Booking at " + reservation.getRestaurant());
            message.setSubject(reservation.getRestaurant().getName() + " - Thank you for your Reservation");

            String cancellationURL = "http://localhost:3000/reservation-confirmed/" + reservation.getId() + "?cancellation=1";

            String msg = "<html>" +
                    "Dear " + reservation.getClientName() + ",<br><br>" +
                    "Your Reservation at <b>" + reservation.getRestaurant().getName() + "</b> for <b>" +
                    reservation.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.US)) +
                    "</b> at <b>" + reservation.getFromTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
                    "</b> was successful. <br><br>" +
                    "You will receive a final confirmation Request via email 24 hours before your reservation.<br>" +
                    "Until then you can cancel your reservation <a href=\"" + cancellationURL + "\">here</a>.<br><br>" +
                    "Thanks a lot, <br>Your EIST-Baer Team" +
                    "</html>";

            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        }
        //Zweite Emailvorlage mit Confirmation Link (< 24 h vor der Buchung versendet)
        else {
            message.setSubject("Mail Subject");

            String msg = "This is my first email using JavaMailer";

            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
        }

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }


}