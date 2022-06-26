package eist.eistbaer.reservationsystem;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(info =
    @Info(
            title = "Reservation-System",
            version = "0.1"
    )
)

@SpringBootApplication
@EnableScheduling
public class ReservationSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReservationSystemApplication.class, args);
    }


}
