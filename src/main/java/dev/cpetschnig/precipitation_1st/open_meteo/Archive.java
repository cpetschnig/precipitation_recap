package dev.cpetschnig.precipitation_1st.open_meteo;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class Archive {

    private final JSONObject json;
    private final ArrayList<HourWithMeasurements> hourlyValues = new ArrayList<HourWithMeasurements>();

    public Archive(JSONObject json) {
        this.json = json;
    }

    public Optional<Double> getPrecipitation(LocalDate date, int hour) {
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

        Optional<HourWithMeasurements> result = hourlyValues.stream()
                .filter(hwm -> hwm.time().toLocalDate().isEqual(date) && hwm.time().getHour() == hour)
                .findFirst();

        return result.map(HourWithMeasurements::precipitation);
    }
}

