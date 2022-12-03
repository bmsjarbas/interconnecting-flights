package br.com.milanes.interconnectingflights.dtos;

import java.util.List;

public class ScheduleDTO {
    private int month;
    private List<ScheduleDayDTO> days;

    private ScheduleDTO() {}
    public ScheduleDTO(int month, List<ScheduleDayDTO> days) {
        this.month = month;
        this.days = days;
    }
    public List<ScheduleDayDTO> getDays() {
        return days;
    }
}