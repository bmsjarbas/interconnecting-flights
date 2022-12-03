package br.com.milanes.interconnectingflights.services;

import br.com.milanes.interconnectingflights.entities.Flight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Service
public class RyanairSchedulingService implements SchedulingService {

    private final FlightService flightService;
    private final RouteService routeService;

    @Autowired
    public RyanairSchedulingService(FlightService flightService, RouteService routeService) {
        this.flightService = flightService;
        this.routeService = routeService;
    }

    @Override
    public Flux<Flight> getAllAvailableFlights(String departureAirport,
                                               String arrivalAirport,
                                               LocalDateTime departureDateTime,
                                               LocalDateTime arrivalDatetime) {
        Flux<Flight> oneStopFlights = routeService
                .getRoute(departureAirport, arrivalAirport)
                .flatMapMany(route -> Flux.fromIterable(route.getAvailableConnectingAirports()))
                .flatMap(connectingAirport -> {
                    Flux<Flight> firstLegs = flightService.getFlights(
                            departureAirport,
                            connectingAirport,
                            departureDateTime,
                            arrivalDatetime);

                    Flux<Flight> connectingLegs = flightService.getFlights(
                            connectingAirport,
                            arrivalAirport,
                            departureDateTime,
                            arrivalDatetime);

                    Flux<Flight> allOneStopFlights = firstLegs.flatMap(firstFlight ->
                                    connectingLegs.map(secondFlight -> new Flight(firstFlight, secondFlight)))
                            .filter(flight -> flight.getStops() == 1);
                    return  allOneStopFlights;

                });
        Flux<Flight> nonStopFlights = flightService.getFlights(
                departureAirport,
                arrivalAirport,
                departureDateTime,
                arrivalDatetime);

        return Flux.concat(nonStopFlights, oneStopFlights);

    }
}
