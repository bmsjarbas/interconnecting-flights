package br.com.milanes.interconnectingflights.services;

import br.com.milanes.interconnectingflights.builders.FlightBuilder;
import br.com.milanes.interconnectingflights.entities.Flight;
import br.com.milanes.interconnectingflights.entities.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WhenGettingAllAvailableFlightsTest {
    @Mock
    private RouteService routeService;
    @Mock
    private FlightService flightService;

    private SchedulingService schedulingService;

    public WhenGettingAllAvailableFlightsTest() {
    }

    @BeforeEach
    void setUp() {
        this.schedulingService = new RyanairSchedulingService(flightService, routeService);
    }

    @Test
    void aListWithDirectAndWithOneStopFlightsAreReturned() {

        String isoDepartureDate = "2023-01-01T09:00:00";
        String isoArrivalDatetime = "2023-01-01T14:00:00";
        LocalDateTime departureDateTime = LocalDateTime.parse(isoDepartureDate);
        LocalDateTime arrivalDatetime = LocalDateTime.parse(isoArrivalDatetime);

        Route dubWroRoute = new Route("DUB", "WRO", Arrays.asList(new String[]{"STN"}));

        Flight flightFromDUBToSTN = new FlightBuilder("DUB",
                "STN",
                2023,
                01,
                01,
                "09:15",
                "10:00")
                .build();

        Flight flightFromSTNToWRO = new FlightBuilder("STN",
                "WRO",
                2023,
                01,
                01,
                "12:00",
                "14:00")
                .build();


        Flight flightFromDUBToWRO = new FlightBuilder("DUB",
                "WRO",
                2023,
                01,
                01,
                "09:00",
                "12:30")
                .build();

        when(routeService.getRoute("DUB", "WRO"))
                .thenReturn(Mono.just(dubWroRoute));
        when(flightService
                .getFlights(
                        "DUB",
                        "WRO",
                        departureDateTime,
                        arrivalDatetime))
                .thenReturn(Flux.just(flightFromDUBToWRO));

        when(flightService.getFlights(
                "DUB",
                "STN",
                departureDateTime,
                arrivalDatetime))
                .thenReturn(Flux.just(flightFromDUBToSTN));

        when(flightService.getFlights(
                "STN",
                "WRO",
                departureDateTime,
                arrivalDatetime))
                .thenReturn(Flux.just(flightFromSTNToWRO));

        Flux<Flight> flights = schedulingService.getAllAvailableFlights(
                "DUB",
                "WRO",
                departureDateTime,
                arrivalDatetime);

        StepVerifier.create(flights)
                .expectNextMatches(flight -> flight.getStops() == 0 &&
                        flight.firstFlightLeg().getDepartureAirport().equals("DUB") &&
                        flight.firstFlightLeg().getArrivalAirport().equals("WRO"))
                .expectNextMatches(flight -> flight.getStops() == 1 &&
                        flight.firstFlightLeg().getDepartureAirport().equals("DUB") &&
                        flight.firstFlightLeg().getArrivalAirport().equals("STN") &&
                        flight.lastFlightLeg().getDepartureAirport().equals("STN") &&
                        flight.lastFlightLeg().getArrivalAirport().equals("WRO"))
                .verifyComplete();
    }


}
