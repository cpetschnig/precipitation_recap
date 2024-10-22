package dev.cpetschnig.precipitation_recap.open_meteo;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;


public class Client {

    static final String OPEN_METEO_HOSTNAME = "archive-api.open-meteo.com";
    static final String OPEN_METEO_ARCHIVE_PATH = "/v1/archive";

    private static final Logger log = LoggerFactory.getLogger(Client.class);
    private HttpClient client = null;

    public Client(HttpClient client) {
        this.client = client;
    }

    public Optional<JSONObject> call(RequestParamsBuilder requestParamsBuilder) {
        HttpRequest request;
        HttpResponse<String> response;

        try {
            request = HttpRequest.newBuilder()
                    .uri(uri(requestParamsBuilder))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
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

    private URI uri(RequestParamsBuilder requestParamsBuilder) throws URISyntaxException {
        return new URI("https", null, OPEN_METEO_HOSTNAME, -1, OPEN_METEO_ARCHIVE_PATH, requestParamsBuilder.uriParams(), null);
    }
}
