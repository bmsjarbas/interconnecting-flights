package br.com.milanes.interconnectingflights.web.models;

import br.com.milanes.interconnectingflights.entities.Flight;

import java.util.List;
import java.util.stream.Collectors;

public class FlightModel {
    public int stops;
    public List<LegModel> legs;

    public FlightModel(Flight entity){
        this.stops = entity.getStops();
        this.legs = entity.getLegs().stream().map(LegModel::new).collect(Collectors.toList());
    }

    private FlightModel(){}
}
