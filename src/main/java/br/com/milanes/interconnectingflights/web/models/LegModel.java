package br.com.milanes.interconnectingflights.web.models;

import br.com.milanes.interconnectingflights.entities.Leg;

import java.time.LocalDateTime;

public class LegModel {
    public final String departureAirport;
    public final String arrivalAirport;
    public final LocalDateTime departureDateTime;

    public final LocalDateTime arrivalDateTime;

    public LegModel(Leg entity) {
        this.departureAirport = entity.getDepartureAirport();
        this.arrivalAirport = entity.getArrivalAirport();
        this.departureDateTime = entity.getDepartureDateTime();
        this.arrivalDateTime = entity.getArrivalDateTime();
    }

}
