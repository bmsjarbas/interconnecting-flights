package br.com.milanes.interconnectingflights.builders;

import br.com.milanes.interconnectingflights.entities.Flight;
import br.com.milanes.interconnectingflights.parsers.FlightDatetime;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FlightBuilder {
    private final String arrivalAirport;
    private final String departureAirport;
    private final LocalDateTime flightDepartureDateTime;
    private final LocalDateTime flightArrivalDateTime;


    public FlightBuilder(String departureFrom,
                         String arrivalAt,
                         int year,
                         int month,
                         int day,
                         String departureTimeAsString,
                         String arrivalTimeAsString) {
        this.arrivalAirport = arrivalAt;
        this.departureAirport = departureFrom;
        LocalTime departureLocalTime = LocalTime.parse(departureTimeAsString);
        LocalTime arrivalLocalTime = LocalTime.parse(arrivalTimeAsString);
        long elapsedMinutes = Duration.between(departureLocalTime, arrivalLocalTime).toMinutes();

        flightDepartureDateTime = FlightDatetime.asLocalDateTime(year,
                month, day, departureTimeAsString);
        flightArrivalDateTime = flightDepartureDateTime.plusMinutes(elapsedMinutes);

    }

    public Flight build() {
        return new Flight(departureAirport, arrivalAirport, flightDepartureDateTime, flightArrivalDateTime);
    }
}
