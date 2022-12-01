package br.com.milanes.interconnectingflights.services;

import br.com.milanes.interconnectingflights.dtos.RouteDTO;
import br.com.milanes.interconnectingflights.entities.Route;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RouteService {
    Flux<RouteDTO> getAvailableRoutes();
    Mono<Route> getRoute(String airportFrom, String airportTo);
}
