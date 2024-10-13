package dev.cpetschnig.precipitation_1st.open_meteo;

import net.minidev.json.JSONObject;

import java.time.LocalDate;

public class Archive {

    private final JSONObject json;

    public Archive(JSONObject json) {
        this.json = json;
    }

    public float getPrecipitation(LocalDate date, int hour) {
        return (float) 0.1;
    }
}

