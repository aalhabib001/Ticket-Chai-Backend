package tech.ticketchai.TicketChaiBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiMsgResponse {

    private int statusCode;

    private String message;
}
