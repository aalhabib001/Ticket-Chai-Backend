package tech.ticketchai.TicketChaiBackend.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public interface AuditorAwareImpl extends AuditorAware {
    @Override
    Optional<String> getCurrentAuditor();

    Optional<Long> getCreatedAt();
}
