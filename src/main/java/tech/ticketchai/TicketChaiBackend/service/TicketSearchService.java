package tech.ticketchai.TicketChaiBackend.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tech.ticketchai.TicketChaiBackend.Repository.TicketRepository;
import tech.ticketchai.TicketChaiBackend.Repository.TripRepository;
import tech.ticketchai.TicketChaiBackend.dto.ApiMsgResponse;
import tech.ticketchai.TicketChaiBackend.dto.ApiResponse;
import tech.ticketchai.TicketChaiBackend.dto.request.TicketBookRequest;
import tech.ticketchai.TicketChaiBackend.dto.response.TicketPurchaseResponse;
import tech.ticketchai.TicketChaiBackend.dto.response.TripResponse;
import tech.ticketchai.TicketChaiBackend.model.TicketModel;
import tech.ticketchai.TicketChaiBackend.model.TripModel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TicketSearchService {
    private final TripRepository tripRepository;
    private final TicketRepository ticketRepository;

    public ResponseEntity<ApiResponse<List<TripModel>>> searchTickets(String from, String to, LocalDate date) {
        TripModel exampleTrip = TripModel.builder()
                .tripStartingPoint(from)
                .tripEndingPoint(to)
                .tripDate(date)
                .build();

        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withMatcher("tripStartingPoint", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("tripEndingPoint", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("tripDate", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        List<TripModel> tripModels = tripRepository.findAll(Example.of(exampleTrip, matcher));

        if (tripModels.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>(404, "No Trip Found", tripModels),
                    HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new ApiResponse<>(404, "Trip Found", tripModels),
                    HttpStatus.OK);
        }
    }

    public ResponseEntity<ApiMsgResponse> purchaseTicket(TicketBookRequest bookRequest) {

        Optional<TripModel> tripModelOptional = tripRepository.findById(bookRequest.getTripId());
        if (tripModelOptional.isPresent()) {
            TripModel tripModel = tripModelOptional.get();

            if (tripModel.getSeatAvailable() < bookRequest.getTotalSeat()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not Enough Seat Available");
            }

            String ticketNo = UUID.randomUUID().toString().toUpperCase().replace("-", "").substring(0, 6);

            int ticketPrice = tripModel.getTicketFare() * bookRequest.getTotalSeat();
            int serviceCharge = (int) ((double) ticketPrice * .05);
            int totalPrice = ticketPrice + serviceCharge;

            TicketModel ticketModel = new TicketModel(ticketNo, bookRequest.getPhoneNo(), bookRequest.getName(),
                    LocalDateTime.now(), bookRequest.getTotalSeat(), tripModel, bookRequest.getAddress(),
                    ticketPrice, serviceCharge, 0, totalPrice, bookRequest.getBoardingPoint(),
                    bookRequest.getDroppingPoint());

            ticketModel.setId(UUID.randomUUID().toString());

            ticketRepository.save(ticketModel);

            return new ResponseEntity<>(new ApiMsgResponse(201, "Ticket Purchase Successful"), HttpStatus.CREATED);

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Trip Found With Id: " + bookRequest.getTripId());
        }

    }

    public ResponseEntity<ApiResponse<List<TicketPurchaseResponse>>> getPurchasedTickets(String phoneNo) {
        List<TicketModel> ticketModels = ticketRepository.findAllByPhoneNo(phoneNo);

        List<TicketPurchaseResponse> ticketPurchaseResponses = new ArrayList<>();

        for (TicketModel ticketModel : ticketModels) {
            TripModel tripModel = ticketModel.getTripModel();
            TripResponse tripResponse = new TripResponse(tripModel.getOperatorName(), tripModel.getTripCode(),
                    tripModel.getTripDate(), tripModel.getTripStartTime(), tripModel.getTripEndTime(), tripModel.getBusType(),
                    tripModel.getTripStartingPoint(), tripModel.getTripEndingPoint(), tripModel.getTicketFare(),
                    tripModel.getSeatType());

            TicketPurchaseResponse ticketPurchaseResponse = new TicketPurchaseResponse(ticketModel.getTicketNo(),
                    ticketModel.getPhoneNo(), ticketModel.getPassenger(), ticketModel.getIssuedOn(),
                    ticketModel.getTotalSeat(), ticketModel.getAddress(), ticketModel.getTicketPrice(),
                    ticketModel.getServiceCharge(), ticketModel.getDiscount(), ticketModel.getTotalPrice(),
                    ticketModel.getBoardingPoint(), ticketModel.getDroppingPoint(), tripResponse);

            ticketPurchaseResponses.add(ticketPurchaseResponse);
        }

        if (!ticketModels.isEmpty()) {
            return new ResponseEntity<>(new ApiResponse<>(200, "Purchased Ticket Found",
                    ticketPurchaseResponses), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse<>(404, "No Purchased Ticket Found",
                    ticketPurchaseResponses), HttpStatus.NOT_FOUND);
        }
    }
}
