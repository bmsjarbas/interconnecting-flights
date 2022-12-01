package br.com.milanes.interconnectingflights.services;

import br.com.milanes.interconnectingflights.dtos.RouteDTO;
import br.com.milanes.interconnectingflights.entities.Route;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RyanairRouteService implements RouteService {
    private WebClient webClient;

    RyanairRouteService(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder
                .baseUrl("https://services-api.ryanair.com")
                .build();
    }

    public Flux<RouteDTO> getAvailableRoutes() {
        return webClient.get().uri("/locate/3/routes")
                .retrieve()
                .bodyToFlux(RouteDTO.class)
                .filter(routeDTO -> routeDTO.getAirportConnectingTo() == null &&
                        routeDTO.getFlightOperator().equals("Ryanair"));
    }

    public Mono<Route> getRoute(String airportFrom, String airportTo) {

        return getAvailableRoutes()
                .filter(dto ->
                        !dto.getAirportFrom().equals(airportFrom) &&
                        dto.getAirportTo().equals(airportTo))
                .map(cr-> cr.getAirportFrom())
                .collectList()
                .map(connectingAirports -> new Route(airportFrom, airportTo, connectingAirports));

    }
}
