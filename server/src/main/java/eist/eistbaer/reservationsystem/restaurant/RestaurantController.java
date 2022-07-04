package eist.eistbaer.reservationsystem.restaurant;

import eist.eistbaer.reservationsystem.reservation.Reservation;
import eist.eistbaer.reservationsystem.reservation.ReservationRepository;
import eist.eistbaer.reservationsystem.restaurant.availableTimeslots.AvailableTimeslot;
import eist.eistbaer.reservationsystem.restaurant.table.RestaurantTable;
import eist.eistbaer.reservationsystem.restaurant.type.RestaurantType;
import eist.eistbaer.reservationsystem.restaurant.type.RestaurantTypeRepository;
import eist.eistbaer.reservationsystem.restaurant.search.RestaurantSearchUtility;
import eist.eistbaer.reservationsystem.restaurant.search.SearchResponse;
import eist.eistbaer.reservationsystem.restaurant.search.SearchRequestBody;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@RestController
@CrossOrigin(origins="*")
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantTypeRepository restaurantTypeRepository;
    private final ReservationRepository reservationRepository;


    public RestaurantController(RestaurantRepository restaurantRepository, RestaurantTypeRepository restaurantTypeRepository, ReservationRepository reservationRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantTypeRepository = restaurantTypeRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/restaurants")
    List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    @GetMapping("/restaurants/{id}")
    Restaurant getRestaurantById(@PathVariable Long id) {
        // TODO Add Error Handling
        return restaurantRepository.findById(id).orElseThrow();
    }

    @PostMapping("/restaurants")
    Restaurant newRestaurant(@RequestBody Restaurant restaurant) {
        RestaurantType type = restaurantTypeRepository.findByName(restaurant.getRestaurantType().getName());
        if (type != null) {
            restaurant.setRestaurantType(type);
        }

        return restaurantRepository.save(restaurant);
    }

    @GetMapping("/restaurants/{id}/available-timeslots")
    List<AvailableTimeslot> getRestaurantAvailableTimeSlots(@PathVariable Long id, @RequestParam String date, @RequestParam String time, @RequestParam int people) {
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();

        LocalDate localDate = LocalDate.parse(date);
        LocalTime localTime = LocalTime.parse(time);

        List<LocalTime> timesToCheck = Arrays.asList(
                localTime.minusMinutes(90),
                localTime.minusMinutes(60),
                localTime.minusMinutes(30),
                localTime,
                localTime.plusMinutes(30),
                localTime.plusMinutes(60),
                localTime.plusMinutes(90)
        );

        List<AvailableTimeslot> timeslots = timesToCheck.stream().map(AvailableTimeslot::new).toList();

        List<RestaurantTable> possibleTables = restaurant.getRestaurantTables().stream()
                .filter(t -> t.getCapacity() >= people)
                .filter(distinctByKey(RestaurantTable::getTableType))
                .toList();

        return timeslots.stream().map(availableTimeslot -> {
            if ((localDate.isAfter(LocalDate.now()) || (localDate.isEqual(LocalDate.now()) && availableTimeslot.getTime().isAfter(LocalTime.now())))
                    && restaurant.hasOpened(localDate, availableTimeslot.getTime(), availableTimeslot.getTime().plusHours(Reservation.DEFAULT_DURATION))) {

                for (RestaurantTable table : possibleTables) {
                    if (table.isFreeBetween(localDate, localTime, localTime.plusHours(Reservation.DEFAULT_DURATION))) {
                        RestaurantTable simpleTable = new RestaurantTable(); // Exclude reservations form Response
                        simpleTable.setId(table.getId());
                        simpleTable.setTableType(table.getTableType());
                        simpleTable.setCapacity(table.getCapacity());
                        simpleTable.setReservations(new ArrayList<>());

                        availableTimeslot.addTable(simpleTable);
                    }
                }
            }

            return availableTimeslot;
        }).toList();
    }

    @RequestMapping(value = "/restaurants/search", method = {RequestMethod.GET, RequestMethod.POST})
    SearchResponse searchRestaurant(@RequestBody SearchRequestBody searchBodyRequest) {
        return RestaurantSearchUtility.search(searchBodyRequest, restaurantRepository, restaurantTypeRepository, reservationRepository);
    }

    // Source: https://stackoverflow.com/a/27872852/9189184 by Stuart Marks
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
