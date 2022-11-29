package br.com.milanes.interconnectingflights.entities;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class Flight {
    private LinkedList<Leg> legs;

    public Flight(String departureAirport,
                  String arrivalAirport,
                  LocalDateTime flightDepartureDateTime,
                  LocalDateTime flightArrivalDateTime) {
        Leg leg = new Leg(departureAirport, arrivalAirport, flightDepartureDateTime, flightArrivalDateTime);
        this.legs = new LinkedList<>();
        legs.add(leg);
    }

    public long getStops() {
        return legs.size() - 1;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public LocalDateTime getDepartureDatetime(){
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
