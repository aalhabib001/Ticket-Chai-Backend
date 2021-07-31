package tech.ticketchai.TicketChaiBackend.jwt.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class GenerateOTPRequest1 {
    String phoneNo;
    int otp;

}
