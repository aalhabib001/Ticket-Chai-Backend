package tech.ticketchai.TicketChaiBackend.jwt.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReferredIdInfoResponse {

    private String name;

    private String username;

    private String email;

    private String phoneNo;

    private List<ReferredIdInfoResponse> nextGens;
}
