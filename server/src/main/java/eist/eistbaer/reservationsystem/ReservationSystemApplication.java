package eist.eistbaer.reservationsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReservationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationSystemApplication.class, args);
    }


}
