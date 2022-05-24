package ru.ideaplatform.ticketservice.interfaces;

import java.text.ParseException;

public interface ITicket {
    public String getOrigin();

    public void setOrigin(String origin);

    public String getOriginName();

    public void setOriginName(String originName);

    public String getDestination();

    public void setDestination(String destination);

    public String getDestinationName();

    public void setDestinationName(String destinationName);

    public String getDepartureDate();

    public void setDepartureDate(String departureDate);

    public String getDepartureTime();

    public void setDepartureTime(String departureTime);

    public String getArrivalDate();

    public void setArrivalDate(String arrivalDate);

    public String getArrivalTime();

    public void setArrivalTime(String arrivalTime);

    public String getCarrier();

    public void setCarrier(String carrier);

    public int getStops();

    public void setStops(int stops);

    public int getPrice();

    public void setPrice(int price);

    public long getTravelDuration() throws ParseException;
}
