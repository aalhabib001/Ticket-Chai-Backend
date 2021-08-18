package tech.ticketchai.TicketChaiBackend.config;

import java.util.Optional;

public interface AuditorAwareImpl {
    Optional<String> getCurrentAuditor();

    Optional<Long> getCreatedAt();
}
