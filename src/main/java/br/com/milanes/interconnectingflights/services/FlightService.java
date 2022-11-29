package br.com.milanes.interconnectingflights.services;

import br.com.milanes.interconnectingflights.builders.FlightBuilder;
import br.com.milanes.interconnectingflights.dtos.FlightDTO;
import br.com.milanes.interconnectingflights.dtos.ScheduleDTO;
import br.com.milanes.interconnectingflights.dtos.ScheduleDayDTO;
import br.com.milanes.interconnectingflights.entities.Flight;
import br.com.milanes.interconnectingflights.entities.Leg;
import br.com.milanes.interconnectingflights.parsers.FlightDatetime;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

@Service
public class FlightService {
    private WebClient webClient;
    public FlightService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("").build();
    }

    public Flux<Flight> getFlights(String departure,
                                          String arrival,
                                          LocalDateTime departureDateTime,
                                          LocalDateTime arrivalDateTime) {

        int flightYear = departureDateTime.getYear();
        int flightMonth = departureDateTime.getMonth().getValue();
        int flightDayOfTheMonth = departureDateTime.getDayOfMonth();
        return this.webClient
                .get()
//                .uri(uriBuilder -> uriBuilder
//                        .path("/schedule/{departure}/{arrival}/years/{year}/months/{month}")
//                        .build(departure, arrival, flightYear, flightMonth))
                .uri("")
                .retrieve()
                .bodyToMono(ScheduleDTO.class)
                .flatMapMany(scheduleDTO-> Flux.fromIterable(scheduleDTO.getDays()))
                .filter(scheduleDayDTO -> scheduleDayDTO.getDay() == flightDayOfTheMonth)
                .singleOrEmpty()
                .flatMapMany(scheduleDayDTO -> Flux.fromIterable(scheduleDayDTO.getFlights()))
                .map(flightDTO -> new FlightBuilder(departure,
                        arrival,
                        flightYear,
                        flightMonth,
                        flightDayOfTheMonth, flightDTO.getDepartureTime(), flightDTO.getArrivalTime()).build())
                .filter(flight -> flight.meetTheSearchCriteria(departureDateTime, arrivalDateTime));

    }
}
