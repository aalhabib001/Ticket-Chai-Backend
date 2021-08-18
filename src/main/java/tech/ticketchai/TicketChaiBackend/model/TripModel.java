package tech.ticketchai.TicketChaiBackend.model;

import lombok.*;
import tech.ticketchai.TicketChaiBackend.model.enums.BusType;
import tech.ticketchai.TicketChaiBackend.model.enums.SeatType;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TRIP_MODEL")
public class TripModel extends BaseEntity {

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

    @CollectionTable
    @ElementCollection
    private List<String> boardingPoints;

    @CollectionTable
    @ElementCollection
    private List<String> endingPoints;

}
