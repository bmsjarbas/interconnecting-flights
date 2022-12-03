package br.com.milanes.interconnectingflights.services.integratedtests;

import br.com.milanes.interconnectingflights.configs.RouteServiceConfiguration;
import br.com.milanes.interconnectingflights.dtos.RouteDTO;
import br.com.milanes.interconnectingflights.services.RouteService;
import br.com.milanes.interconnectingflights.services.RyanairRouteService;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static br.com.milanes.interconnectingflights.helpers.JSONHelpers.getJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class WhenGettingRoutesFromExternalAPI {
    MockWebServer mockWebServer;
    private RouteService routeService;
    private RouteServiceConfiguration routeServiceConfiguration;


    @BeforeEach
    public void setupMockServer() throws IOException {
        routeServiceConfiguration = new RouteServiceConfiguration();
        mockWebServer = new MockWebServer();

        routeServiceConfiguration.setServerAddress(String.format("http://%s:%s",
                mockWebServer.getHostName(),
                mockWebServer.getPort()));

        routeService = new RyanairRouteService(WebClient.builder(), routeServiceConfiguration);

    }

    @Test
    void aListOfRouteDTOsIsReturned() throws InterruptedException {
        String routePath = "/locate/3/routes";

        mockWebServer.enqueue(
                new MockResponse().setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(getJson("all-routes.json"))
        );

        List<RouteDTO> routes = routeService.getAvailableRoutes().collectList().block();;
        RecordedRequest request = mockWebServer.takeRequest();

        assertEquals(2, routes.size());
        assertEquals("GET", request.getMethod());
        assertEquals("/locate/3/routes", request.getPath());
        assertTrue(routes
                .stream()
                .anyMatch(routeDTO -> routeDTO.getAirportFrom().equalsIgnoreCase("DUB") &&
                        routeDTO.getAirportTo().equalsIgnoreCase("KUN")));
        assertTrue(routes
                .stream()
                .anyMatch(routeDTO -> routeDTO.getAirportFrom().equalsIgnoreCase("AAL") &&
                        routeDTO.getAirportTo().equalsIgnoreCase("ARN")));

        assertTrue(routes.stream().allMatch(routeDTO -> routeDTO
                .getOperator()
                .equalsIgnoreCase("ryanair")));

    }


}
