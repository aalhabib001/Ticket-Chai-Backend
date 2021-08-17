package tech.ticketchai.TicketChaiBackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ticketchai.TicketChaiBackend.dto.ApiMsgResponse;
import tech.ticketchai.TicketChaiBackend.dto.request.TripCreateRequest;
import tech.ticketchai.TicketChaiBackend.service.TripService;

@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/admin/bus-trips")
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<ApiMsgResponse> createTrip(@RequestHeader(name = "Authorization") String jwtToken,
                                                     @RequestBody TripCreateRequest tripCreateRequest) {
        return tripService.createTrip(jwtToken, tripCreateRequest);
    }
}
