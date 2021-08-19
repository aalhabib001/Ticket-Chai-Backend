package tech.ticketchai.TicketChaiBackend.model;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BOOKED_TICKET_MODEL")
public class TicketModel extends BaseEntity {

    private String ticketNo;

    private String phoneNo;

    private String passenger;

    private LocalDateTime issuedOn;

    private Integer totalSeat;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    private TripModel tripModel;

    private String address;

    private Integer ticketPrice;

    private Integer serviceCharge;

    private Integer discount;

    private Integer totalPrice;

    private String boardingPoint;

    private String droppingPoint;

}
