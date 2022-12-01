package br.com.milanes.interconnectingflights.services;

import br.com.milanes.interconnectingflights.entities.Flight;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Service
public interface SchedulingService {
    public Flux<Flight> getAllAvailableFlights(String departureAirport,
                                               String arrivalAirport,
                                               LocalDateTime departureDateTime, LocalDateTime arrivalDatetime);
}
