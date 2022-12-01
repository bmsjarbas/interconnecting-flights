package br.com.milanes.interconnectingflights.entitties;

import br.com.milanes.interconnectingflights.builders.FlightBuilder;
import br.com.milanes.interconnectingflights.entities.Flight;
import br.com.milanes.interconnectingflights.entities.Leg;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WhenCreatingAConnectingFlight {

    @Test
    void aFlightWithTwoLegsIsCreated() {
        Flight flightFromDUBToSTN = new FlightBuilder("DUB",
                "STN",
                2023,
                01,
                01,
                "09:15",
                "10:00")
                .build();

        Flight flightFromSTNToWRO = new FlightBuilder("STN",
                "WRO",
                2023,
                01,
                01,
                "12:00",
                "14:00")
                .build();
        var flight = new Flight(flightFromDUBToSTN, flightFromSTNToWRO);
        Leg firstFlightLeg = flight.firstFlightLeg();
        Leg connectingFlightLeg = flight.lastFlightLeg();
        assertEquals(1, flight.getStops());
        assertEquals("DUB", firstFlightLeg.getDepartureAirport());
        assertEquals("STN", firstFlightLeg.getArrivalAirport());
        assertEquals("STN", connectingFlightLeg.getDepartureAirport());
        assertEquals("WRO", connectingFlightLeg.getArrivalAirport());

    }
}
