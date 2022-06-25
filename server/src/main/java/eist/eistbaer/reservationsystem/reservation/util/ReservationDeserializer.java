package eist.eistbaer.reservationsystem.reservation.util;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import eist.eistbaer.reservationsystem.reservation.Reservation;
import eist.eistbaer.reservationsystem.restaurant.RestaurantRepository;
import eist.eistbaer.reservationsystem.restaurant.table.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationDeserializer extends JsonDeserializer<Reservation> {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Override
    public Reservation deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.getCodec().readTree(p);

        Reservation reservation = new Reservation();

        reservation.setClientName(node.get("clientName").asText());
        reservation.setClientEmail(node.get("clientEmail").asText());
        reservation.setFromTime(LocalTime.parse(node.get("fromTime").asText()));
        reservation.setDate(LocalDate.parse(node.get("date").asText()));
        reservation.setPeople(node.get("people").asInt());
        reservation.setTable(restaurantTableRepository.findById(node.get("restaurantTable").asLong()).orElseThrow(IllegalArgumentException::new));
        reservation.setRestaurant(restaurantRepository.findById(node.get("restaurant").asLong()).orElseThrow(IllegalArgumentException::new));

        return reservation;
    }
}
