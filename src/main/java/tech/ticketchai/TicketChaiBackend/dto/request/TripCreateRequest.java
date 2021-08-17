package tech.ticketchai.TicketChaiBackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.ticketchai.TicketChaiBackend.model.enums.BusType;
import tech.ticketchai.TicketChaiBackend.model.enums.SeatType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TripCreateRequest {

    private String operatorName;

    private String tripCode;

    private LocalDate tripDate;

    private String tripStartTime;

    private String tripEndTime;

    private BusType busType;

    private String tripStartingPoint;

    private String tripEndingPoint;

    private Integer ticketFare;

    private Integer totalSeat;

    private SeatType seatType;

    private List<String> boardingPoints;

    private List<String> endingPoints;

}
