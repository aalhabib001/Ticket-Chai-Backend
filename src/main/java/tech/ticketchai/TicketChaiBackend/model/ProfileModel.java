package tech.ticketchai.TicketChaiBackend.model;

import lombok.*;
import org.hibernate.annotations.NaturalId;
import tech.ticketchai.TicketChaiBackend.jwt.model.UserModel;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profile_model")
public class ProfileModel {

    @Id
    private String id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    private Long createdOn;

    @Column(unique = true)
    @NaturalId(mutable = true)
    private String phoneNo;

    @Column(unique = true)
    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    @Column(unique = true)
    @NaturalId(mutable = true)
    @Size(min = 11, max = 50)
    @Email
    private String email;

    private Boolean isDeleted;

    private Boolean isActive;

    private Long otpCreatedOn;

    private Integer otp;

    @OneToOne(cascade = CascadeType.ALL)
    private UserModel userModel;


}
