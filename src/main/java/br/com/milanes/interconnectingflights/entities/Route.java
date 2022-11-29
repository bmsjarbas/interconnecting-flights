package br.com.milanes.interconnectingflights.entities;

import java.util.List;

public class Route {
    private String departureAirport;
    private String arrivalAirport;
    private List<String> availableConnectingAirports;


    public Route(String airportFrom, String airportTo, List<String> connectingAirports) {
        this.departureAirport = airportFrom;
        this.arrivalAirport = airportTo;
        this.availableConnectingAirports = connectingAirports;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public List<String> getAvailableConnectingAirports() {
        return availableConnectingAirports;
    }
}
