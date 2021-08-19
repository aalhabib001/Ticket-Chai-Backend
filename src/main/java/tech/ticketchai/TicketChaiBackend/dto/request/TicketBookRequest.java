package tech.ticketchai.TicketChaiBackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketBookRequest {

    private String name;

    private String phoneNo;

    private String tripId;

    private String address;

    private Integer totalSeat;

    private String boardingPoint;

    private String droppingPoint;
}
