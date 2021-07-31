package tech.ticketchai.TicketChaiBackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.ticketchai.TicketChaiBackend.model.ProfileModel;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileModel, String> {

    boolean existsByPhoneNo(String phoneNo);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<ProfileModel> findByUsername(String username);

    Optional<ProfileModel> findByPhoneNo(String phoneNo);

    boolean existsByUsernameAndIsDeleted(String userName, Boolean isDeleted);

    boolean existsByUsernameAndIsActive(String userName, Boolean isActive);

}
