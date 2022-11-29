package br.com.milanes.interconnectingflights.services;

import br.com.milanes.interconnectingflights.dtos.FlightDTO;
import br.com.milanes.interconnectingflights.dtos.RouteDTO;
import br.com.milanes.interconnectingflights.dtos.ScheduleDTO;
import br.com.milanes.interconnectingflights.dtos.ScheduleDayDTO;
import br.com.milanes.interconnectingflights.entities.Flight;
import br.com.milanes.interconnectingflights.entities.Leg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WhenGettingNonStopsFlights {
    @Mock
    private WebClient.Builder webClientBuilder;
    @Mock
    private WebClient webClientMock;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;
    @Mock
    private WebClient.ResponseSpec responseMock;

    private FlightService flightService;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.baseUrl(anyString()).build()).thenReturn(webClientMock);
        this.flightService = new FlightService(webClientBuilder);
    }

    @Test
    void aListOfFlightsGivenTheFiltersAreReturned(){

        FlightDTO flight = new FlightDTO("FR",
                "1616",
                "09:00",
                "13:00");

        FlightDTO earlierFlight = new FlightDTO("FR",
                "1616",
                "08:00",
                "13:00");
        ScheduleDayDTO scheduleDayDTO = new ScheduleDayDTO(1, Arrays.asList(new FlightDTO[] { flight, earlierFlight })) ;
        ScheduleDTO scheduleDTO = new ScheduleDTO(1, Arrays.asList(new ScheduleDayDTO[] { scheduleDayDTO}));

        //flight 2022-11-29 19:00 - 23:30 ,
        //flight 2022-11-29 11:00 - 14:00
        String isoDepartureDate = "2023-01-01T09:00:00";
        String isoArrivalDatetime = "2023-01-01T13:00:00";
        LocalDateTime departureTimeForFirstFlight = LocalDateTime.parse(isoDepartureDate);
        LocalDateTime arrivalDatetimeForFirstFlight = LocalDateTime.parse(isoArrivalDatetime);

        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(""))
                .thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        when(responseMock.bodyToMono(ScheduleDTO.class)).thenReturn(Mono.just(scheduleDTO));
        Flux<Flight> flights = flightService.getFlights("DUB",
                "WRO",
                departureTimeForFirstFlight,
                arrivalDatetimeForFirstFlight);

        StepVerifier.create(flights)
                .expectNextMatches(f ->
                        f.getStops() == 0 &&
                        f.getArrivalDatetime().isEqual(arrivalDatetimeForFirstFlight) &&
                        f.getDepartureDatetime().isEqual(departureTimeForFirstFlight)
                )
                .expectComplete()
                .verify();
    }

}
