package br.com.milanes.interconnectingflights.services;

import br.com.milanes.interconnectingflights.dtos.FlightDTO;
import br.com.milanes.interconnectingflights.dtos.ScheduleDTO;
import br.com.milanes.interconnectingflights.dtos.ScheduleDayDTO;
import br.com.milanes.interconnectingflights.entities.Flight;
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
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyString;
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

    private RyanairFlightService flightService;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.baseUrl(anyString()).build()).thenReturn(webClientMock);
        this.flightService = new RyanairFlightService(webClientBuilder);
    }

    @Test
    void aListOfFlightsGivenTheFiltersAreReturned(){
        String departureAirport = "DUB";
        String arrivalAirport = "WRO";
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

        String isoDepartureDate = "2023-01-01T09:00:00";
        String isoArrivalDatetime = "2023-01-01T13:00:00";
        LocalDateTime departureTimeForFirstFlight = LocalDateTime.parse(isoDepartureDate);
        LocalDateTime arrivalDatetimeForFirstFlight = LocalDateTime.parse(isoArrivalDatetime);
        String uri = String.format("/timtbl/3/schedules/%s/%s/years/%s/months/%s",
                departureAirport,
                arrivalAirport,
                departureTimeForFirstFlight.getYear(),
                departureTimeForFirstFlight.getMonth().getValue());

        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(uri))
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
