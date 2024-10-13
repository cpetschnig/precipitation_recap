package dev.cpetschnig.precipitation_1st.open_meteo;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestArchive {

    static private JSONObject json;

    static private String jsonString() throws IOException {
        return IOUtils.toString(TestArchive.class.getResourceAsStream("/open-meteo/open-meteo_sample_response.json"), "UTF-8");
    }

    static private JSONObject jsonContent() throws IOException, ParseException {
        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        return (JSONObject) parser.parse(jsonString());
    }


    @BeforeAll
    static void loadJsonFile() throws IOException, ParseException {
        json = jsonContent();
    }

    @Test
    void testGetPrecipitation() {
        Archive archive = new Archive(TestArchive.json);
        float result = archive.getPrecipitation(LocalDate.of(2024, Month.OCTOBER, 7), 13);
        assertEquals(0.10, result, 0.000001);
    }
}
