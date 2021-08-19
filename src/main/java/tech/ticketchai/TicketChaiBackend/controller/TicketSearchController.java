package tech.ticketchai.TicketChaiBackend.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.ticketchai.TicketChaiBackend.dto.ApiMsgResponse;
import tech.ticketchai.TicketChaiBackend.dto.ApiResponse;
import tech.ticketchai.TicketChaiBackend.dto.request.TicketBookRequest;
import tech.ticketchai.TicketChaiBackend.dto.response.TicketPurchaseResponse;
import tech.ticketchai.TicketChaiBackend.model.TicketModel;
import tech.ticketchai.TicketChaiBackend.model.TripModel;
import tech.ticketchai.TicketChaiBackend.service.TicketSearchService;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/user/tickets")
public class TicketSearchController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TicketSearchController.class);
    private final TicketSearchService ticketSearchService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TripModel>>> searchTickets(
            @RequestParam String from, @RequestParam String to,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        LOGGER.info("Processing date: {}", date);
        return ticketSearchService.searchTickets(from, to, date);
    }

    @PostMapping("/purchase")
    public ResponseEntity<ApiMsgResponse> purchaseTicket(@RequestBody TicketBookRequest ticketBookRequest) {

        return ticketSearchService.purchaseTicket(ticketBookRequest);
    }

    @GetMapping("/purchase")
    public ResponseEntity<ApiResponse<List<TicketPurchaseResponse>>> getPurchasedTickets(@RequestParam String phoneNo){
        return ticketSearchService.getPurchasedTickets(phoneNo);
    }

}
