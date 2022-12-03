package br.com.milanes.interconnectingflights.services;

import br.com.milanes.interconnectingflights.configs.RouteServiceConfiguration;
import br.com.milanes.interconnectingflights.dtos.RouteDTO;
import br.com.milanes.interconnectingflights.entities.Route;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WhenGettingAllAvailableRoutes {

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
    RouteServiceConfiguration routeServiceConfiguration;

    RouteService routeService;

    @BeforeEach
    void setUp() {

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.baseUrl(anyString()).build()).thenReturn(webClientMock);
        when(webClientMock.get()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri("/locate/3/routes")).thenReturn(requestHeadersMock);
        when(requestHeadersMock.retrieve()).thenReturn(responseMock);
        routeServiceConfiguration = new RouteServiceConfiguration();
        routeServiceConfiguration.setServerAddress("http://localhost/");
        this.routeService = new RyanairRouteService(webClientBuilder, routeServiceConfiguration);
    }

    @Test
    void onlyRoutesOperatedByRyanairShouldBeReturned() {

        RouteDTO routeOperatedByRyanair = new RouteDTO(
                "DUB",
                "MAD",
                null,
                "Ryanair");

        RouteDTO secondRouteOperatedByRyanairDTO = new RouteDTO(
                "DUB",
                "LHR",
                null,
                "Ryanair");

        RouteDTO routeDoesntOperatedByRyanair = new RouteDTO(
                "MAD",
                "DUB",
                null,
                "FakeAirline");

        RouteDTO[] routeDTOS = new RouteDTO[] { routeOperatedByRyanair , routeDoesntOperatedByRyanair, secondRouteOperatedByRyanairDTO};

        when(responseMock.bodyToFlux(RouteDTO.class)).thenReturn(Flux.just(routeDTOS));
        Flux<RouteDTO> actualRoutes = this.routeService.getAvailableRoutes();
        StepVerifier.create(actualRoutes)
                .expectNext(routeOperatedByRyanair)
                .expectNext(secondRouteOperatedByRyanairDTO)
                .expectComplete()
                .verify();

    }

    @Test
    void onlyRoutesWithAirportConnectingIsNullAreListed() {
        RouteDTO routeOperatedByRyanair = new RouteDTO(
                "DUB",
                "MAD",
                null,
                "Ryanair");

        RouteDTO secondRouteOperatedByRyanairDTO = new RouteDTO(
                "DUB",
                "LHR",
                null,
                "Ryanair");

        RouteDTO routeDTODoesntOperatedByRyanair = new RouteDTO(
                "MAD",
                "DUB",
                null,
                "FakeAirline");

        RouteDTO[] routes = new RouteDTO[] { routeOperatedByRyanair , routeDTODoesntOperatedByRyanair, secondRouteOperatedByRyanairDTO};

        when(responseMock.bodyToFlux(RouteDTO.class)).thenReturn(Flux.just(routes));
        Mono<Route> actualRoute = this.routeService.getRoute("DUB", "MAD");
        StepVerifier.create(actualRoute)
                .expectNextMatches(expectedRoute ->
                                expectedRoute.getDepartureAirport().equals("DUB") &&
                                expectedRoute.getArrivalAirport().equals("MAD") &&
                                expectedRoute.getAvailableConnectingAirports().isEmpty()
                        )
                .expectComplete()
                .verify();
    }

    @Test
    void nonStopsRoutesAndConnectingRoutesAreListed() {
        RouteDTO routeOperatedByRyanair = new RouteDTO(
                "DUB",
                "MAD",
                null,
                "Ryanair");

        RouteDTO secondRouteOperatedByRyanairDTO = new RouteDTO(
                "DUB",
                "STN",
                null,
                "Ryanair");

        RouteDTO thirdRouteOperatedByRyanair = new RouteDTO(
                "STN",
                "MAD",
                null,
                "Ryanair");

        RouteDTO routeDTODoesntOperatedByRyanair = new RouteDTO(
                "MAD",
                "DUB",
                null,
                "FakeAirline");

        RouteDTO[] routes = new RouteDTO[] { routeOperatedByRyanair ,
                routeDTODoesntOperatedByRyanair,
                secondRouteOperatedByRyanairDTO,
                thirdRouteOperatedByRyanair
        };


        when(responseMock.bodyToFlux(RouteDTO.class)).thenReturn(Flux.just(routes));
        Mono<Route> actualRoute = this.routeService.getRoute("DUB", "MAD");
        StepVerifier.create(actualRoute)
                .expectNextMatches(expectedRoute ->
                        expectedRoute.getDepartureAirport().equals("DUB") &&
                                expectedRoute.getArrivalAirport().equals("MAD") &&
                                expectedRoute.getAvailableConnectingAirports().size() == 1 &&
                                expectedRoute.getAvailableConnectingAirports().get(0).equals("STN")
                )
                .expectComplete()
                .verify();
    }
}
