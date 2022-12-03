package br.com.milanes.interconnectingflights.web.e2etests;

import br.com.milanes.interconnectingflights.web.models.FlightModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WhenGettingFlightsGivenSpecificRoutes {
    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp(){
        webTestClient = webTestClient.mutate()
                .responseTimeout(Duration.ofMillis(30000))
                .build();

    }

    @Test
    void aListOfFlightsIsReturned() {
        webTestClient
                .get()
                .uri(builder -> builder.path("/interconnections")
                        .queryParam("departure", "DUB")
                        .queryParam("arrival", "STN")
                        .queryParam("departureDateTime", "2023-07-01T07:00:00")
                        .queryParam("arrivalDateTime", "2023-07-01T12:00:00")
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(FlightModel.class).value(flights -> {
                    assertTrue(flights.size() > 0);
                });
    }
}
