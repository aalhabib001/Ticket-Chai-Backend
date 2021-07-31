package tech.ticketchai.TicketChaiBackend.jwt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Getter
@Setter
@AllArgsConstructor
public class SignUpForm {

    @NotBlank
    @Size(min = 3, max = 50)
    private String name;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 60)
    private String email;

    @NotNull
    @Size(min = 3)
    private String userName;

    @NotNull
    @Size(min = 3)
    private String phoneNo;

    @NotBlank
    @Size(min = 8, max = 40)
    private String password;

    private Integer otp;

}