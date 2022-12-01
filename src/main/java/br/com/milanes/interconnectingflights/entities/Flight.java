package br.com.milanes.interconnectingflights.entities;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Flight {
    private LinkedList<Leg> legs;
    public static final int MIN_HOURS_INTERVAL_TO_CONNECTING = 2;

    public Leg firstFlightLeg() {
        return this.legs.getFirst();
    }
    public Leg lastFlightLeg() {
        return this.legs.getLast();
    }

    public Flight(String departureAirport,
                  String arrivalAirport,
                  LocalDateTime flightDepartureDateTime,
                  LocalDateTime flightArrivalDateTime) {
        Leg leg = new Leg(departureAirport, arrivalAirport, flightDepartureDateTime, flightArrivalDateTime);
        this.legs = new LinkedList<>();
        legs.add(leg);
    }

    public Flight(Flight firstFlight, Flight connectingFlight) {
        this.legs = new LinkedList<>();
        legs.add(firstFlight.firstFlightLeg());
        addConnectingLeg(connectingFlight.firstFlightLeg());
    }

    private void addConnectingLeg(Leg connectingLeg) {
        if (legs.size() != 1) {
            return;
        }
        Leg firstLeg = legs.getFirst();
        LocalDateTime expectedDepartureTimeOfConnectingFlight = firstLeg
                .getArrivalDateTime()
                .plusHours(MIN_HOURS_INTERVAL_TO_CONNECTING);

        if ((connectingLeg.getDepartureDateTime().isAfter(expectedDepartureTimeOfConnectingFlight) ||
                connectingLeg.getDepartureDateTime().isEqual(expectedDepartureTimeOfConnectingFlight))) {
            legs.add(connectingLeg);
        }

    }

    public long getStops() {
        return legs.size() - 1;
    }

    public LocalDateTime getDepartureDatetime() {
        return legs.getFirst().getDepartureDateTime();
    }

    public LocalDateTime getArrivalDatetime() {
        return legs.getLast().getArrivalDateTime();
    }

    public boolean meetTheSearchCriteria(LocalDateTime expectedDepartureTIme, LocalDateTime expectedArrivalTime) {
        LocalDateTime departureDatetime = legs.getFirst().getDepartureDateTime();
        LocalDateTime arrivalDateTime = legs.getLast().getArrivalDateTime();
        return (departureDatetime.isEqual(expectedDepartureTIme) || departureDatetime.isAfter(expectedDepartureTIme)) &&
                (arrivalDateTime.isEqual(expectedArrivalTime) || arrivalDateTime.isBefore(expectedArrivalTime));
    }
}
