package eist.eistbaer.reservationsystem.reservation.email;

import eist.eistbaer.reservationsystem.reservation.Reservation;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.net.URI;
import java.util.Date;
import java.util.Properties;

public class EmailUtils {

    //attributes
    private String username;
    private String password;

    //attributes for mailcontent
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

    /*
        sendet eine Mail mittels des hinterlegten Postfaches
        -> falls cancellationRequest = true, dann wird die erste Mailvorlage mit Link zum Wiederruf versendet
        -> falls cancellationRequest = false, dann wird die zweite Mailvorlage mit Link zum Bestätigen in den letzten 24h vor der Reservierung versendet
         */
    public void sendMail(boolean cancellationRequest, Reservation reservation) throws Exception {

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
        if(cancellationRequest){
            message.setSubject("1st Reservation Notice - Booking at "+reservation.getRestaurant());

            String cancellationURL = "localhost:8080/reservations/email/cancellation/"+reservation.getId();

            String msg = "Dear "+reservation.getClientName()+",\n your Reservation at "+reservation.getRestaurant().getName()+" at the following date "+reservation.getFromTime()+" has been sucessfull. You will receive a final confirmation Request via email 24 hours before your reservation.\n Until then you can cancel your reservation via the following link: "+cancellationURL+"\n Thanks a lot. \n Your EIST-Baer Team";


            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        }
        //Zweite Emailvorlage mit Confirmation Link (< 24 h vor der Buchung versendet)
        else{
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