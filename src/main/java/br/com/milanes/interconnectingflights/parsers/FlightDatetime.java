package br.com.milanes.interconnectingflights.parsers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class FlightDatetime {
    public static LocalDateTime asLocalDateTime(int year, int month, int day, String timeAsString) {
        LocalTime flightTime =  LocalTime.parse(timeAsString);
        LocalDate flightDate = LocalDate.of(year, month, day);
        return LocalDateTime.of(flightDate, flightTime);
    }
}
