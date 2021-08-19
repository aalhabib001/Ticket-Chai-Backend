package tech.ticketchai.TicketChaiBackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tech.ticketchai.TicketChaiBackend.model.TripModel;
import tech.ticketchai.TicketChaiBackend.model.enums.BusType;
import tech.ticketchai.TicketChaiBackend.model.enums.SeatType;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketPurchaseResponse {
    private String ticketNo;

    private String phoneNo;

    private String passenger;

    private LocalDateTime issuedOn;

    private Integer totalSeat;

    private String address;

    private Integer ticketPrice;

    private Integer serviceCharge;

    private Integer discount;

    private Integer totalPrice;

    private String boardingPoint;

    private String droppingPoint;

    private TripResponse tripDetails;
}
