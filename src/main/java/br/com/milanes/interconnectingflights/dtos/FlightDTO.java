package br.com.milanes.interconnectingflights.dtos;

public class FlightDTO {
    private String carrierCode;
    private String number;
    private String departureTime;
    private String arrivalTime;

    public FlightDTO(String carrierCode, String number, String departureTime, String arrivalTime) {
        this.carrierCode = carrierCode;
        this.number = number;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    private FlightDTO() {}
    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }
}
