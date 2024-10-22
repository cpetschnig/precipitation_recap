package dev.cpetschnig.precipitation_1st.formatter;

import dev.cpetschnig.precipitation_1st.open_meteo.Archive;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestCliOutputFormatter {

    private final Archive mockedArchive = mock(Archive.class);

    @Test
    @DisplayName("Regular output of formatter")
    public void testPrint() {
        LocalDate startDate = LocalDate.of(2024, 10, 3);
        LocalDate endDate = startDate.plusDays(1);

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
}
