package tech.ticketchai.TicketChaiBackend.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tech.ticketchai.TicketChaiBackend.Repository.TripRepository;
import tech.ticketchai.TicketChaiBackend.dto.ApiResponse;
import tech.ticketchai.TicketChaiBackend.model.TripModel;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class TicketSearchService {
    private final TripRepository tripRepository;

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
}
