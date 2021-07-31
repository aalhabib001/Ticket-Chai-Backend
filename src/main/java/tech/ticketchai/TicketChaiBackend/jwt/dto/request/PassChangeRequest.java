package tech.ticketchai.TicketChaiBackend.jwt.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor

public class PassChangeRequest {
    @NotBlank
    @Size(min = 8, max = 40)
    String oldPass;

    @NotBlank
    @Size(min = 8, max = 40)
    String newPass;
}
