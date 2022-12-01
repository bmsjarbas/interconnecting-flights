package br.com.milanes.interconnectingflights.services;

import br.com.milanes.interconnectingflights.entities.Flight;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface FlightService {
    Flux<Flight> getFlights(String departure,
                            String arrival,
                            LocalDateTime departureDateTime,
                            LocalDateTime arrivalDateTime);
}
