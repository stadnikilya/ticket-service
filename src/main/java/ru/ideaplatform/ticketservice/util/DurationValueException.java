package ru.ideaplatform.ticketservice.util;

public class DurationValueException extends Exception {
    private final long duration;

    public long getDuration() {
        return duration;
    }

    public DurationValueException(String message, long duration) {
        super(message);
        this.duration = duration;
    }
}
