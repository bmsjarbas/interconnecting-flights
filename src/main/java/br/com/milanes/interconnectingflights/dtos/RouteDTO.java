package br.com.milanes.interconnectingflights.dtos;

public class RouteDTO {
    private String airportFrom;
    private String airportTo;
    private String airportConnectingTo;
    private String flightOperator;

    public RouteDTO(String airportFrom, String airportTo, String airportConnectingTo, String flightOperator) {
        this.airportFrom = airportFrom;
        this.airportTo = airportTo;
        this.airportConnectingTo = airportConnectingTo;
        this.flightOperator = flightOperator;
    }

    public String getAirportFrom() {
        return airportFrom;
    }

    public void setAirportFrom(String airportFrom) {
        this.airportFrom = airportFrom;
    }

    public String getAirportTo() {
        return airportTo;
    }

    public void setAirportTo(String airportTo) {
        this.airportTo = airportTo;
    }

    public String getAirportConnectingTo() {
        return airportConnectingTo;
    }

    public void setAirportConnectingTo(String airportConnectingTo) {
        this.airportConnectingTo = airportConnectingTo;
    }

    public String getFlightOperator() {
        return flightOperator;
    }

    public void setFlightOperator(String flightOperator) {
        this.flightOperator = flightOperator;
    }
}
