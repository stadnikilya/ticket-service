package ru.ideaplatform.ticketservice;

import ru.ideaplatform.ticketservice.util.Util;

import java.util.List;

public class Main {
    public static final String PATH = ".\\src\\main\\resources\\tickets.json";
    public static final String ORIGIN = "VVO";
    public static final String DESTINATION = "TLV";
    public static final int PERCENT = 90;
    public static void main(String[] args) throws Exception {
        List<Ticket> tickets = Util.getTicketsFromJson(PATH);
        tickets = Util.filterTickets(tickets, ORIGIN, DESTINATION);

        long averageTravelDuration = Util.getAverageTravelDuration(tickets);
        String averageTravelDurationString = Util.getStringDurationFromMillis(averageTravelDuration);

        long percentile = Util.getPercentile(tickets, PERCENT);
        String percentileString = Util.getStringDurationFromMillis(percentile);

        System.out.printf("среднее время полета между городами Владивосток и Тель-Авив %s%n", averageTravelDurationString);
        System.out.printf("90-й процентиль времени полета между городами  Владивосток и Тель-Авив %s%n", percentileString);
    }
}
