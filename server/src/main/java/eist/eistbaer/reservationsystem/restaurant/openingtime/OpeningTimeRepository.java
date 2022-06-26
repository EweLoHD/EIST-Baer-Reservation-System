package eist.eistbaer.reservationsystem.restaurant.openingtime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpeningTimeRepository extends JpaRepository<OpeningTime, Long> {
}


