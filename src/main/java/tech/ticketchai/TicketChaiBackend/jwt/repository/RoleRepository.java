package tech.ticketchai.TicketChaiBackend.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.ticketchai.TicketChaiBackend.jwt.model.Role;
import tech.ticketchai.TicketChaiBackend.jwt.model.RoleName;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // Optional<Role> findByName(RoleName roleName);
    Optional<Role> findByName(RoleName role);
}