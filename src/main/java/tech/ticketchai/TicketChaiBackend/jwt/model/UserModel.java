package tech.ticketchai.TicketChaiBackend.jwt.model;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import tech.ticketchai.TicketChaiBackend.model.ProfileModel;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "users_model")
public class UserModel {

    @Id
    private String id;

    boolean isVerified;

    @Column(unique = true)
    @NotBlank
    @Size(min = 3, max = 100)
    private String username;

    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    private ProfileModel profileModel;

    @ColumnDefault("false")
    private Boolean isDeleted;
}