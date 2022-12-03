package br.com.milanes.interconnectingflights.services;

import br.com.milanes.interconnectingflights.builders.FlightBuilder;
import br.com.milanes.interconnectingflights.dtos.ScheduleDTO;
import br.com.milanes.interconnectingflights.entities.Flight;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Service
public class RyanairFlightService implements FlightService {
    private WebClient webClient;
    public RyanairFlightService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://services-api.ryanair.com").build();
    }

    @Override
    public Flux<Flight> getFlights(String departure,
                                   String arrival,
                                   LocalDateTime departureDateTime,
                                   LocalDateTime arrivalDateTime) {

        int flightYear = departureDateTime.getYear();
        int flightMonth = departureDateTime.getMonth().getValue();
        int flightDayOfTheMonth = departureDateTime.getDayOfMonth();
        String uri = String.format("/timtbl/3/schedules/%s/%s/years/%s/months/%s",
                departure,
                arrival,
                flightYear,
                flightMonth);
        return this.webClient
                .get()
                .uri(uri)
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
