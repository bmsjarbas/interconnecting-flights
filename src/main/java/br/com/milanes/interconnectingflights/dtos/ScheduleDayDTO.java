package br.com.milanes.interconnectingflights.dtos;

import java.util.List;

public class ScheduleDayDTO {
    private int day;
    private List<FlightDTO> flights;

    private ScheduleDayDTO() {}
    public ScheduleDayDTO(int day, List<FlightDTO> flights) {
        this.day = day;
        this.flights = flights;
    }

    public int getDay() {
        return day;
    }

    public List<FlightDTO> getFlights() {
        return flights;
    }
}
