package tech.ticketchai.TicketChaiBackend.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.ticketchai.TicketChaiBackend.Repository.TripRepository;
import tech.ticketchai.TicketChaiBackend.dto.ApiMsgResponse;
import tech.ticketchai.TicketChaiBackend.dto.request.TripCreateRequest;
import tech.ticketchai.TicketChaiBackend.model.TripModel;

import java.util.UUID;

@AllArgsConstructor
@Service
public class TripService {

    private final TripRepository tripRepository;

    public ResponseEntity<ApiMsgResponse> createTrip(String jwtToken, TripCreateRequest tripCreateRequest) {

        TripModel tripModel = new TripModel(
                tripCreateRequest.getOperatorName(), tripCreateRequest.getTripCode(), tripCreateRequest.getTripDate(),
                tripCreateRequest.getTripStartTime(), tripCreateRequest.getTripEndTime(), tripCreateRequest.getBusType(),
                tripCreateRequest.getTripStartingPoint(), tripCreateRequest.getTripEndingPoint(),
                tripCreateRequest.getTicketFare(), tripCreateRequest.getTotalSeat(), tripCreateRequest.getSeatType(),
                tripCreateRequest.getBoardingPoints(), tripCreateRequest.getEndingPoints());

        tripModel.setId(UUID.randomUUID().toString());

        tripRepository.save(tripModel);

        return new ResponseEntity<>(new ApiMsgResponse(201, "Trip Created"), HttpStatus.CREATED);
    }
}
