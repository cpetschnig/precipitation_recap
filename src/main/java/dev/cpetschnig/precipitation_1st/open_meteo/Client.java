package dev.cpetschnig.precipitation_1st.open_meteo;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Client {

    private final HttpClient client = HttpClient.newHttpClient();

//    public Client() {}

    public boolean call() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://archive-api.open-meteo.com/v1/archive?latitude=52.52&longitude=13.41&start_date=2024-09-24&end_date=2024-10-08&hourly=temperature_2m,precipitation"))
                .build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
            System.err.println(e.getMessage());
            System.err.println(e.toString());
            return false;
        }

        System.out.println("response body: " + response.body());
        return true;
    }
}
