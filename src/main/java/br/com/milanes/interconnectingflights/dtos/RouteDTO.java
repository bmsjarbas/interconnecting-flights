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

    public String getAirportTo() {
        return airportTo;
    }
    public String getAirportConnectingTo() {
        return airportConnectingTo;
    }

    public String getFlightOperator() {
        return flightOperator;
    }
}
