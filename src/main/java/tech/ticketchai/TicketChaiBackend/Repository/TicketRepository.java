package tech.ticketchai.TicketChaiBackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.ticketchai.TicketChaiBackend.model.TicketModel;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<TicketModel, String> {

    List<TicketModel> findAllByPhoneNo(String phoneNo);
}
