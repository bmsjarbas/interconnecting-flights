package br.com.milanes.interconnectingflights.web.handlers;

import br.com.milanes.interconnectingflights.services.RouteService;
import br.com.milanes.interconnectingflights.services.SchedulingService;
import br.com.milanes.interconnectingflights.web.models.FlightModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class InterconnectingHandler {

    private final SchedulingService schedulingService;
    private final RouteService routeService;

    @Autowired
    public InterconnectingHandler(SchedulingService schedulingService, RouteService routeService) {
        this.schedulingService = schedulingService;
        this.routeService = routeService;
    }

    public Mono<ServerResponse>  getInterconnectingFlights(ServerRequest serverRequest) {
        String arrivalDateTimeAsString = serverRequest.queryParam("arrivalDateTime").orElse("");
        String departureDateTimeAsString = serverRequest.queryParam("departureDateTime").orElse("");;
        String departure = serverRequest.queryParam("departure").orElse("");
        String arrival = serverRequest.queryParam("arrival").orElse("");
        LocalDateTime departureDateTime = LocalDateTime.parse(departureDateTimeAsString);
        LocalDateTime arrivalDateTime = LocalDateTime.parse(arrivalDateTimeAsString);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(schedulingService.getAllAvailableFlights(departure,
                                arrival,
                                departureDateTime,
                                arrivalDateTime)
                                .map(flight -> new FlightModel(flight))
                        ,FlightModel.class);
    }
}
