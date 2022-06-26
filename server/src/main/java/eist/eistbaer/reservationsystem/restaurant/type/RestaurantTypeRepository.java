package eist.eistbaer.reservationsystem.restaurant.type;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantTypeRepository extends JpaRepository<RestaurantType, Long> {


    RestaurantType findByName(String name);

}
