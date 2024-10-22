package dev.cpetschnig.precipitation_recap.formatter;

import dev.cpetschnig.precipitation_recap.open_meteo.Archive;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestCliOutputFormatter {

    private final Archive mockedArchive = mock(Archive.class);

    private final LocalDate startDate = LocalDate.of(2024, 10, 3);
    private final LocalDate endDate = startDate.plusDays(1);

    @Test
    @DisplayName("Regular output of formatter")
    public void testPrint() {
        double[] startDayValues = new double[]{
                0.00, 0.00, 0.00, 0.00, 0.00, 0.10, 0.20, 0.00, 0.00, 0.00, 0.00, 0.00,
                0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.50, 0.90, 0.90, 0.90, 1.50, 3.61};
        double[] endDayValues = new double[]{
                0.00, 0.30, 1.82, 0.70, 0.10, 0.10, 0.20, 0.00, 0.00, 0.00, 0.00, 0.00,
                0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.50, 0.90, 0.90, 0.90, 1.50, 0.60};

        when(mockedArchive.getPrecipitationForDay(startDate)).thenReturn(startDayValues);
        when(mockedArchive.getPrecipitationForDay(endDate)).thenReturn(endDayValues);

        CliOutputFormatter subject = new CliOutputFormatter(mockedArchive, startDate, endDate);
        subject.print();
    }

    @Test
    @DisplayName("Use custom output function")
    public void testSetOutput() {
        double[] values = new double[]{
                0.00, 0.00, 0.00, 0.00, 0.00, 0.10, 0.20, 0.00, 0.00, 0.00, 0.00, 0.00,
                0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.50, 0.90, 0.90, 0.90, 1.50, 3.60};

        when(mockedArchive.getPrecipitationForDay(startDate)).thenReturn(values);

        CliOutputFormatter subject = new CliOutputFormatter(mockedArchive, startDate, startDate);

        var stringContainer = new ArrayList();
        Consumer<String> consumer = stringContainer::add;

        subject.setOutput(consumer);
        subject.print();

        Object[] result = stringContainer.stream().map(str -> str.toString().replaceAll("^\\s+|\\s+$", "")).toArray();

        assertEquals("2024-10-03:", result[0]);
        assertEquals("█", result[1]);
        assertEquals("▄    █", result[2]);
        assertEquals("_    _    _    _    _    ▄    ▄    _    _    _    _    _    _    _    _    _    _    _    ▄    █    █    █    █    █", result[3]);
        assertEquals("0.0  0.0  0.0  0.0  0.0  0.1  0.2  0.0  0.0  0.0  0.0  0.0  0.0  0.0  0.0  0.0  0.0  0.0  0.5  0.9  0.9  0.9  1.5  3.6", result[4]);
    }
}
