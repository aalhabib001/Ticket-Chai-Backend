package tech.ticketchai.TicketChaiBackend.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.ticketchai.TicketChaiBackend.jwt.model.UserModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {
    Optional<UserModel> findByUsername(String username);

    Optional<UserModel> findByEmail(String email);

    boolean existsByUsernameAndIsDeleted(@NotBlank @Size(min = 3, max = 100) String username, Boolean isDeleted);
}