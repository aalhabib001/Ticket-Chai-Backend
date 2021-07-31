package tech.ticketchai.TicketChaiBackend.jwt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "registration_otp_model")
public class RegistrationOtpModel {
    @Id
    private String id;

    private Integer otp;

    private Long createdOn;

    private String phoneNo;
}
