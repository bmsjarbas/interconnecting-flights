package br.com.milanes.interconnectingflights.services.integratedtests;

import br.com.milanes.interconnectingflights.configs.FlightServiceConfiguration;
import br.com.milanes.interconnectingflights.entities.Flight;
import br.com.milanes.interconnectingflights.services.FlightService;
import br.com.milanes.interconnectingflights.services.RyanairFlightService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static br.com.milanes.interconnectingflights.helpers.JSONHelpers.getJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WhenGettingFlightsGivenARouteFromExternalAPITest {

    MockWebServer mockWebServer;
    private FlightService flightService;
    private FlightServiceConfiguration flightServiceConfiguration;


    @BeforeEach
    public void setupMockServer() throws IOException {
        flightServiceConfiguration = new FlightServiceConfiguration();
        mockWebServer = new MockWebServer();

        flightServiceConfiguration.setServerAddress(String.format("http://%s:%s",
                mockWebServer.getHostName(),
                mockWebServer.getPort()));

        flightService = new RyanairFlightService(WebClient.builder(), flightServiceConfiguration);
    }

    @Test
    public void listAllFlightsThatMeetsTheCriteria() {
        mockWebServer.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(getJson("dub-stn-flights-202307.json"))
        );

        LocalDateTime departureDateTime = LocalDateTime.parse("2023-07-01T07:00:00");
        LocalDateTime arrivalDateTime =  LocalDateTime.parse("2023-07-01T10:00:00");
        List<Flight> flights = flightService.getFlights(
                "DUB",
                "STN",
                departureDateTime,
                arrivalDateTime).collectList().block();
        assertEquals(1, flights.stream().count());
        Flight dubStnFlight = flights.get(0);
        assertEquals(0, dubStnFlight.getStops());
        assertEquals(LocalDateTime.parse("2023-07-01T07:45"),  dubStnFlight.getDepartureDatetime());
        assertEquals(LocalDateTime.parse("2023-07-01T09:05"),  dubStnFlight.getArrivalDatetime());

    }
}
