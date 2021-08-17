package tech.ticketchai.TicketChaiBackend.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.ticketchai.TicketChaiBackend.Repository.TripRepository;
import tech.ticketchai.TicketChaiBackend.dto.ApiMsgResponse;
import tech.ticketchai.TicketChaiBackend.dto.request.TripCreateRequest;
import tech.ticketchai.TicketChaiBackend.model.TripModel;

@AllArgsConstructor
@Service
public class TripService {

    private final TripRepository tripRepository;

    public ResponseEntity<ApiMsgResponse> createTrip(String jwtToken, TripCreateRequest tripCreateRequest) {

        TripModel tripModel = new TripModel();


        return null;
    }
}
