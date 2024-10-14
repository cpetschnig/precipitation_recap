package dev.cpetschnig.precipitation_1st.open_meteo;

import java.time.LocalDateTime;

public record HourWithMeasurements(LocalDateTime time, Double temperature, Double precipitation) {
}
