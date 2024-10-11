package dev.cpetschnig.precipitation_1st.open_meteo;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;


public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private HttpClient client = null;

    public Client(HttpClient client) {
        this.client = client;
    }

    public Optional<JSONObject> call() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://archive-api.open-meteo.com/v1/archive?latitude=52.52&longitude=13.41&start_date=2024-09-24&end_date=2024-10-08&hourly=temperature_2m,precipitation"))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            log.error(String.valueOf(e));
            return Optional.empty();
        }

        if (response.statusCode() != 200) {
            return Optional.empty();
        }

        JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
        try {
            return Optional.ofNullable((JSONObject) parser.parse(response.body()));
        } catch (ParseException e) {
            log.error(String.valueOf(e));
        }
        return Optional.empty();
    }
}
