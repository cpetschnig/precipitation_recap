package dev.cpetschnig.precipitation_recap.open_meteo;

import java.time.LocalDateTime;

public record HourWithMeasurements(LocalDateTime time, Double temperature, Double precipitation) {
}
