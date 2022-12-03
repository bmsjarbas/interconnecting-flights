package br.com.milanes.interconnectingflights.web.actuator;

import br.com.milanes.interconnectingflights.configs.FlightServiceConfiguration;
import br.com.milanes.interconnectingflights.configs.RouteServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component

public class RyanairRouteEndpointServiceHealthIndicator implements ReactiveHealthIndicator {

    private WebClient.Builder webClientBuilder;

    private FlightServiceConfiguration flightServiceConfiguration;
    private RouteServiceConfiguration routeServiceConfiguration;

    @Autowired
    public RyanairRouteEndpointServiceHealthIndicator(WebClient.Builder webclientBuilder, FlightServiceConfiguration flightServiceConfiguration,
                                                      RouteServiceConfiguration routeServiceConfiguration) {
        this.flightServiceConfiguration = flightServiceConfiguration;
        this.routeServiceConfiguration = routeServiceConfiguration;
        this.webClientBuilder = webclientBuilder;

    }

    @Override
    public Mono<Health> health() {
        return webClientBuilder
                .baseUrl(routeServiceConfiguration.getServerAddress())
                .build().get().uri("/locate/3/routes")
                .retrieve().toBodilessEntity().map(s -> new Health.Builder().up().build())
                .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
                .log();
    }
}