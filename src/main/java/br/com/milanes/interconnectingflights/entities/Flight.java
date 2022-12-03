package br.com.milanes.interconnectingflights.entities;

import java.time.LocalDateTime;
import java.util.LinkedList;

public class Flight {
    private LinkedList<Leg> legs;
    public static final int MIN_HOURS_INTERVAL_TO_CONNECTING = 2;

    public Leg firstFlightLeg() {
        return this.getLegs().getFirst();
    }
    public Leg lastFlightLeg() {
        return this.getLegs().getLast();
    }

    public Flight(String departureAirport,
                  String arrivalAirport,
                  LocalDateTime flightDepartureDateTime,
                  LocalDateTime flightArrivalDateTime) {
        Leg leg = new Leg(departureAirport, arrivalAirport, flightDepartureDateTime, flightArrivalDateTime);
        this.legs = new LinkedList<>();
        getLegs().add(leg);
    }

    public Flight(Flight firstFlight, Flight connectingFlight) {
        this.legs = new LinkedList<>();
        getLegs().add(firstFlight.firstFlightLeg());
        addConnectingLeg(connectingFlight.firstFlightLeg());
    }

    private void addConnectingLeg(Leg connectingLeg) {
        if (getLegs().size() != 1) {
            return;
        }
        Leg firstLeg = getLegs().getFirst();
        LocalDateTime expectedDepartureTimeOfConnectingFlight = firstLeg
                .getArrivalDateTime()
                .plusHours(MIN_HOURS_INTERVAL_TO_CONNECTING);

        if ((connectingLeg.getDepartureDateTime().isAfter(expectedDepartureTimeOfConnectingFlight) ||
                connectingLeg.getDepartureDateTime().isEqual(expectedDepartureTimeOfConnectingFlight))) {
            getLegs().add(connectingLeg);
        }

    }

    public int getStops() {
        return getLegs().size() - 1;
    }

    public LocalDateTime getDepartureDatetime() {
        return getLegs().getFirst().getDepartureDateTime();
    }

    public LocalDateTime getArrivalDatetime() {
        return getLegs().getLast().getArrivalDateTime();
    }

    public boolean meetTheSearchCriteria(LocalDateTime expectedDepartureTIme, LocalDateTime expectedArrivalTime) {
        LocalDateTime departureDatetime = getLegs().getFirst().getDepartureDateTime();
        LocalDateTime arrivalDateTime = getLegs().getLast().getArrivalDateTime();
        return (departureDatetime.isEqual(expectedDepartureTIme) || departureDatetime.isAfter(expectedDepartureTIme)) &&
                (arrivalDateTime.isEqual(expectedArrivalTime) || arrivalDateTime.isBefore(expectedArrivalTime));
    }

    public LinkedList<Leg> getLegs() {
        return this.legs;
    }
}
