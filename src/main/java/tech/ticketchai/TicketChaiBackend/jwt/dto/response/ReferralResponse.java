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
public class ReferralResponse {
    private ReferredIdInfoResponse previousGen;

    private List<ReferredIdInfoResponse> nextGens;
}
