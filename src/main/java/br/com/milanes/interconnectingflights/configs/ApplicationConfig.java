package br.com.milanes.interconnectingflights.configs;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RouteServiceConfiguration.class)
public class ApplicationConfig {
}
