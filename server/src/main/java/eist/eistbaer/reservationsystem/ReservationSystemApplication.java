package eist.eistbaer.reservationsystem;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eist.eistbaer.reservationsystem.restaurant.Restaurant;
import eist.eistbaer.reservationsystem.restaurant.RestaurantRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@OpenAPIDefinition(info =
    @Info(
            title = "Reservation-System",
            version = "0.1"
    )
)

@SpringBootApplication
@EnableScheduling
public class ReservationSystemApplication {

    private static final Logger log = LoggerFactory.getLogger(ReservationSystemApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ReservationSystemApplication.class, args);
    }

    /**
     * ONLY USED FOR FILLING THE DATABASE ONCE. Import Restaurants from json Files located in /restaurants
     * @param restaurantRepository RestaurantRepository
     * @return CommandLineRunner
     */
    @Bean
    CommandLineRunner importRestaurants(RestaurantRepository restaurantRepository) {
        return args -> {
            ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

            Files.walk(Paths.get("restaurants")).filter(Files::isRegularFile).forEach(path -> {
                //log.info(path.toAbsolutePath().toString());
                try {
                    Restaurant restaurant = objectMapper.readValue(path.toAbsolutePath().toFile(), Restaurant.class);

                    if(restaurantRepository.findByName(restaurant.getName()).size() == 0) {
                        log.info("Importing Restaurant " + restaurantRepository.save(restaurant).getName());
                    }
                } catch (IOException e) {
                    if (e instanceof JsonParseException jsonParseException) {
                        log.error(path.toAbsolutePath().toString() + " : \n" + jsonParseException.getMessage().replace("\n", ""));
                    }
                }
            });
        };
    }
}
