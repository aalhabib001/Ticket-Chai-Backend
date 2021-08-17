package tech.ticketchai.TicketChaiBackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.ticketchai.TicketChaiBackend.model.TripModel;

public interface TripRepository extends JpaRepository<TripModel, String> {


}
