package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws Exception {
        String path = ".\\src\\main\\resources\\tickets.json";
        String origin = "VVO";
        String destination = "TLV";

        List<Ticket> tickets = new ArrayList<>();

        // сreate ticket objects from json
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, ArrayList<Ticket>> ticketsMap = objectMapper.readValue((Paths.get(path)).toFile(),
                    new TypeReference<Map<String, ArrayList<Ticket>>>() {});
            tickets = ticketsMap.get("tickets");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // filter tickets by departure and arrival points
        tickets = tickets.stream()
                .filter(ticket -> ticket.getOrigin().equals(origin) && ticket.getDestination().equals(destination))
                .collect(Collectors.toList());

        long averageTravelDuration = getAverageTravelDuration(tickets);
        String averageTravelDurationString = getStringDurationFromMillis(averageTravelDuration);

        // create list of durations for percentile calculation
        List<Long> travelTimeDurations = new ArrayList<>();
        for (Ticket ticket : tickets) {
            try {
                travelTimeDurations.add(ticket.getTravelDuration());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        long ninetiethPercentile = getPercentile(travelTimeDurations, 90);
        String ninetiethPercentileString = getStringDurationFromMillis(ninetiethPercentile);

        System.out.printf("среднее время полета между городами Владивосток и Тель-Авив %s%n", averageTravelDurationString);
        System.out.printf("90-й процентиль времени полета между городами  Владивосток и Тель-Авив %s%n", ninetiethPercentileString);
    }

    public static long getAverageTravelDuration(List<Ticket> tickets) {
        if (tickets.size() == 0) {
            return 0;
        }

        long sumTravelTimes;
        List<Long> travelTimes = new ArrayList<>();

        for (Ticket ticket : tickets) {
            try {
                travelTimes.add(ticket.getTravelDuration());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        sumTravelTimes = travelTimes.stream()
                .mapToLong(Long::longValue)
                .sum();

        return sumTravelTimes / travelTimes.size();
    }

    public static long getPercentile(List<Long> durations, double percentile) {
        if (durations.size() == 0) {
            return 0;
        }
        if (percentile == 0) {
            return durations.size();
        }
        Collections.sort(durations);
        int index = (int) Math.ceil(percentile / 100.0 * durations.size());
        return durations.get(index - 1);
    }

    public static String getStringDurationFromMillis(Long millisDuration) throws Exception {
        if (millisDuration == 0) {
            return "0 дней 0 часов 0 минут";
        }
        if (millisDuration < 0) {
            throw new Exception("Продолжительность полета не может быть отрицательной");
        }
        long days = TimeUnit.MILLISECONDS.toDays(millisDuration);
        long hours = TimeUnit.MILLISECONDS.toHours(millisDuration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisDuration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisDuration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisDuration));
        return String.format("%d дней %d часов %d минут", days, hours, minutes);
    }
}
