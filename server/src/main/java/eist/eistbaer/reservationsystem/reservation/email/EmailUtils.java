package eist.eistbaer.reservationsystem.reservation.email;

import eist.eistbaer.reservationsystem.reservation.Reservation;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.UnsupportedEncodingException;
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

    /**
     * E-Mail, that gets send right after a new Reservation is created
     *
     * @param reservation The given Reservation
     */
    public void sendCompletedMail(Reservation reservation) {
        String cancellationURL = "http://localhost:3000/reservation-confirmed/" + reservation.getId() + "?cancellation=1";

        String subject = reservation.getRestaurant().getName() + " - Thank you for your Reservation";
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

        this.sendMailAsync(reservation.getClientEmail(), subject, msg);
    }

    /**
     * E-Mail, that gets send 24h before the Reservation
     *
     * @param reservation The given Reservation
     */
    public void sendConfirmationMail(Reservation reservation) {
        String confirmationURL = "http://localhost:3000/reservation-confirmed/" + reservation.getId() + "?confirmation=1";

        String subject = "Confirm your Reservation at " + reservation.getRestaurant().getName();
        String msg = "<html>" +
                "Dear " + reservation.getClientName() + ",<br><br>" +
                "Your Reservation at <b>" + reservation.getRestaurant().getName() + "</b> for <b>" +
                reservation.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.US)) +
                "</b> at <b>" + reservation.getFromTime().format(DateTimeFormatter.ofPattern("HH:mm")) +
                "</b> needs to be confirmed! <br><br>" +
                "You have to confirm your Reservation during the next 12h <a href=\"" + confirmationURL + "\">here</a>, otherwise it will \n" +
                "automatically be canceled!<br><br>" +
                "See you tomorrow, <br>Your " + reservation.getRestaurant().getName() + " Team" +
                "</html>";

        this.sendMailAsync(reservation.getClientEmail(), subject, msg);
    }

    public void sendMailAsync(String address, String subject, String msg) {
        new Thread(() -> {
            try {
                this.sendMail(address, subject, msg);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void sendMail(String address, String subject, String msg) throws MessagingException {

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress("eist_table_booking@web.de", "EIST-Baer Reservation System"));
        } catch (UnsupportedEncodingException e) {
            message.setFrom(new InternetAddress("eist_table_booking@web.de"));
            throw new RuntimeException(e);
        }
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));

        MimeBodyPart mimeBodyPart = new MimeBodyPart();

        message.setSubject(subject);
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
    }


}