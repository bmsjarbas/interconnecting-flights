package br.com.milanes.interconnectingflights.dtos;

import java.util.List;

public class ScheduleDTO {
    private int month;
    private List<ScheduleDayDTO> days;

    public ScheduleDTO(int month, List<ScheduleDayDTO> days) {
        this.month = month;
        this.days = days;
    }

    public int getMonth() {
        return month;
    }

    public List<ScheduleDayDTO> getDays() {
        return days;
    }
}






