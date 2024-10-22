package dev.cpetschnig.precipitation_recap.open_meteo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRequestParamsBuilder {

    @Test
    @DisplayName("Build URI with valid params")
    void testUriParams() {
        LocalDate startDate = LocalDate.of(2024, Month.OCTOBER, 15);
        LocalDate endDate = startDate.plusWeeks(1);
        RequestParamsBuilder requestParamsBuilder = RequestParamsBuilder.create(46.68, 14.35, startDate, endDate);
        String result = requestParamsBuilder.uriParams();
        assertEquals("latitude=46.68&longitude=14.35&start_date=2024-10-15&end_date=2024-10-22&hourly=temperature_2m,precipitation", result);
    }
}
