package tech.ticketchai.TicketChaiBackend.jwt.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import tech.ticketchai.TicketChaiBackend.jwt.model.RegistrationOtpModel;

import java.util.Optional;

public interface RegistrationOtpRepository extends JpaRepository<RegistrationOtpModel, String > {
    Optional<RegistrationOtpModel> findByPhoneNo(String phoneNo);

    Optional<RegistrationOtpModel> findByPhoneNoAndOtp(String phoneNo, Integer otp);
}
