package dev.cpetschnig.precipitation_recap.open_meteo;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class Archive {

    private final ArrayList<HourWithMeasurements> hourlyValues = new ArrayList<HourWithMeasurements>();

    public Archive(JSONObject json) {
        buildHourlyValueData(json);
    }

    public Optional<Double> getPrecipitation(LocalDate date, int hour) {
        Optional<HourWithMeasurements> result = hourlyValues.stream()
                .filter(hwm -> hwm.time().toLocalDate().isEqual(date) && hwm.time().getHour() == hour)
                .findFirst();

        return result.map(HourWithMeasurements::precipitation);
    }

    public double[] getPrecipitationForDay(LocalDate date) {
        double[] result = new double[24];
        Object[] intermediate = hourlyValues.stream()
                .filter(hwm -> hwm.time().toLocalDate().isEqual(date))
                .map(HourWithMeasurements::precipitation)
                .toArray();

        for (int i = 0; i < 24; i++) {
            result[i] = (double) intermediate[i];
        }

        return result;
    }

    private void buildHourlyValueData(JSONObject json) {
        JSONObject hourly = (JSONObject) json.get("hourly");

        JSONArray times = (JSONArray) hourly.get("time");
        JSONArray temperatures = (JSONArray) hourly.get("temperature_2m");
        JSONArray precipitations = (JSONArray) hourly.get("precipitation");

        for (int i = 0; i < times.size(); i++) {
            LocalDateTime time = LocalDateTime.parse(times.get(i).toString());
            double temperature = (double) temperatures.get(i);
            double precipitation = (double) precipitations.get(i);
            HourWithMeasurements hwm = new HourWithMeasurements(time, temperature, precipitation);
            hourlyValues.add(hwm);
        }
    }
}

