package dev.cpetschnig.precipitation_recap.open_meteo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RequestParamsBuilder {

    private final double latitude;
    private final double longitude;
    private final LocalDate startDate;
    private final LocalDate endDate;

    static public RequestParamsBuilder create(double latitude, double longitude, LocalDate startDate, LocalDate endDate) {
        return new RequestParamsBuilder(latitude, longitude, startDate, endDate);
    };

    private RequestParamsBuilder(double latitude, double longitude, LocalDate startDate, LocalDate endDate) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String uriParams() {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String[] params = new String[]{
                String.format("latitude=%.2f", latitude),
                String.format("longitude=%.2f", longitude),
                "start_date=" + startDate.format(dateTimeFormat),
                "end_date=" + endDate.format(dateTimeFormat),
                "hourly=temperature_2m,precipitation"
        };

        return String.join("&", params);
    }
}
