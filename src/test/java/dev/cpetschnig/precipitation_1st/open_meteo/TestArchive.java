package dev.cpetschnig.precipitation_1st.open_meteo;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
    @DisplayName("Query precipitation for single hour with valid params")
    void testGetPrecipitation() {
        Archive archive = new Archive(TestArchive.json);
        double result = archive.getPrecipitation(LocalDate.of(2024, Month.OCTOBER, 7), 13).orElse(0.0);
        assertEquals(0.10, result, 0.000001);

        result = archive.getPrecipitation(LocalDate.of(2024, Month.OCTOBER, 8), 23).orElse(0.0);
        assertEquals(3.60, result, 0.000001);
    }

    @Test
    @DisplayName("Query precipitation for single hour with invalid date")
    void testGetPrecipitationWithInvalidDate() {
        Archive archive = new Archive(TestArchive.json);
        Optional<Double> result = archive.getPrecipitation(LocalDate.of(1975, Month.JANUARY, 1), 0);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Query precipitation for single day with valid params")
    void testGetPrecipitationForDay() {
        Archive archive = new Archive(TestArchive.json);
        double[] expected = new double[]{0.00, 0.00, 0.00, 0.00, 0.00, 0.10, 0.20, 0.00, 0.00, 0.00, 0.00, 0.00,
                                         0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.50, 0.90, 0.90, 0.90, 1.50, 3.60};
        double[] result = archive.getPrecipitationForDay(LocalDate.of(2024, Month.OCTOBER, 8));
        assertArrayEquals(expected, result);
    }
}
