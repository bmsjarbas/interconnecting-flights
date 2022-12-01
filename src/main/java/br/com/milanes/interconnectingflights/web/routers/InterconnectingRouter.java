package br.com.milanes.interconnectingflights.web.routers;

import br.com.milanes.interconnectingflights.web.handlers.InterconnectingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration(proxyBeanMethods = false)
public class InterconnectingRouter {

    @Bean
    public RouterFunction<ServerResponse> interconnectingFlightsRoute(InterconnectingHandler handler) {
        return RouterFunctions.route(
                GET("/interconnections")
                        .and(accept(MediaType.APPLICATION_JSON))
                ,
                handler::getInterconnectingFlights);
    }
}
