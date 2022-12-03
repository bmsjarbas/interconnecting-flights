package br.com.milanes.interconnectingflights.web.actuator;

import br.com.milanes.interconnectingflights.configs.FlightServiceConfiguration;
import br.com.milanes.interconnectingflights.configs.RouteServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Component
public class RyanairScheduleEndpointService implements ReactiveHealthIndicator {
    private WebClient.Builder webClientBuilder;

    private FlightServiceConfiguration flightServiceConfiguration;

    @Autowired
    public RyanairScheduleEndpointService(WebClient.Builder webclientBuilder, FlightServiceConfiguration flightServiceConfiguration,
                                                      RouteServiceConfiguration routeServiceConfiguration) {
        this.flightServiceConfiguration = flightServiceConfiguration;
        this.webClientBuilder = webclientBuilder;

    }

    @Override
    public Mono<Health> health() {


        LocalDateTime dateTimeNow = LocalDateTime.now();
        String uri = String.format("/timtbl/3/schedules/%s/%s/years/%s/months/%s",
                "DUB",
                "WRO",
                dateTimeNow.getYear(),
                dateTimeNow.getMonth().getValue());
        return webClientBuilder
                .baseUrl(flightServiceConfiguration.getServerAddress())
                .build().get().uri(uri)
                .retrieve().toBodilessEntity().map(s -> new Health.Builder().up().build())
                .onErrorResume(ex -> Mono.just(new Health.Builder().down(ex).build()))
                .log();
    }
}
