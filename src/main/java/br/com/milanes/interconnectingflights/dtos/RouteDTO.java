package br.com.milanes.interconnectingflights.dtos;

public class RouteDTO {
    private String airportFrom;
    private String airportTo;
    private String connectingAirport;
    private String operator;

    public RouteDTO(String airportFrom, String airportTo, String connectingAirport, String operator) {
        this.airportFrom = airportFrom;
        this.airportTo = airportTo;
        this.connectingAirport = connectingAirport;
        this.operator = operator;
    }

    public RouteDTO(){}

    public String getAirportFrom() {
        return airportFrom;
    }

    public String getAirportTo() {
        return airportTo;
    }
    public String getConnectingAirport() {
        return connectingAirport;
    }

    public String getOperator() {
        return operator;
    }
}
