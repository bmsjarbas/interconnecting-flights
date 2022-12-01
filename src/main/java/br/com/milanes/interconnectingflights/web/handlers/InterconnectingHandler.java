package br.com.milanes.interconnectingflights.web.handlers;

import br.com.milanes.interconnectingflights.entities.Flight;
import br.com.milanes.interconnectingflights.services.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class InterconnectingHandler {

    private final SchedulingService schedulingService;

    @Autowired
    public InterconnectingHandler(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    public Mono<ServerResponse>  getInterconnectingFlights(ServerRequest serverRequest) {
        String arrivalDateTimeAsString = serverRequest.queryParam("arrivalDateTime").orElse("");
        String departureDateTimeAsString = serverRequest.queryParam("departureDateTime").orElse("");;
        String departure = serverRequest.queryParam("departure").orElse("");
        String arrival = serverRequest.queryParam("arrival").orElse("");
        LocalDateTime departureDateTime = LocalDateTime.parse(departureDateTimeAsString);
        LocalDateTime arrivalDateTime = LocalDateTime.parse(arrivalDateTimeAsString);
        System.out.println("HEY");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(schedulingService.getAllAvailableFlights(departure, arrival, departureDateTime, arrivalDateTime),
                        Flight.class);
    }
}
