package ru.ideaplatform.ticketservice.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.ideaplatform.ticketservice.Ticket;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Util {

    private Util() {
        throw new IllegalStateException("Utility class");
    }
    public static long getAverageTravelDuration(List<Ticket> tickets) {
        if (tickets.isEmpty()) {
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

    public static long getPercentile(List<Ticket> tickets, double percent) {
        List<Long> durations = new ArrayList<>();
        for (Ticket ticket : tickets) {
            try {
                durations.add(ticket.getTravelDuration());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Collections.sort(durations);

        if (tickets.isEmpty()) {
            return 0;
        } else if (percent == 0) {
            return durations.get(durations.size() - 1) + 1;
        }

        int index = (int) Math.ceil(percent / 100.0 * durations.size());

        return durations.get(index - 1);
    }

    public static String getStringDurationFromMillis(Long millisDuration) throws DurationValueException {
        if (millisDuration == 0) {
            return "0 дней 0 часов 0 минут";
        }
        if (millisDuration < 0) {
            throw new DurationValueException("Продолжительность полета не может быть отрицательной", millisDuration);
        }
        long days = TimeUnit.MILLISECONDS.toDays(millisDuration);
        long hours = TimeUnit.MILLISECONDS.toHours(millisDuration) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisDuration));
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millisDuration) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisDuration));
        return String.format("%d дней %d часов %d минут", days, hours, minutes);
    }

    public static List<Ticket> getTicketsFromJson(String path) {
        List<Ticket> tickets = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            File file = new File(path);
            Map<String, ArrayList<Ticket>> ticketsMap = objectMapper.readValue(file,
                    new TypeReference<Map<String, ArrayList<Ticket>>>() {});
            tickets = ticketsMap.get("tickets");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tickets;
    }

    public static List<Ticket> filterTickets(List<Ticket> tickets, String origin, String destination) {
        tickets = tickets.stream()
                .filter(ticket -> ticket.getOrigin().equals(origin) && ticket.getDestination().equals(destination))
                .collect(Collectors.toList());
        return tickets;
    }
}
