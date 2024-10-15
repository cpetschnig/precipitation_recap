package dev.cpetschnig.precipitation_1st.open_meteo;

import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class TestClient extends Mockito {

    private final HttpClient httpClient = mock(HttpClient.class);
    private final HttpResponse<String> mockedResponse = mock(HttpResponse.class);
    private final RequestParamsBuilder mockedRequestParamsBuilder = mock(RequestParamsBuilder.class);

    private String jsonString() throws IOException {
        return IOUtils.toString(
                this.getClass().getResourceAsStream("/open-meteo/open-meteo_sample_response.json"), "UTF-8"
        );
    }

    @Test
    void returnsAValidJsonObject() throws IOException, InterruptedException {
        when(httpClient.send(any(), any(HttpResponse.BodyHandler.class))).thenReturn(mockedResponse);
        when(mockedResponse.body()).thenReturn(jsonString());
        when(mockedResponse.statusCode()).thenReturn(200);

        Client client = new Client(httpClient);

        Optional<JSONObject> opt = client.call(mockedRequestParamsBuilder);
        assertTrue(opt.isPresent());
        assertEquals("GMT", opt.get().getAsString("timezone"));
    }

    @Test
    void returnsAnEmptyOptionalUponConnectionError() throws IOException, InterruptedException {
        when(httpClient.send(any(), any(HttpResponse.BodyHandler.class))).thenThrow(java.net.ConnectException.class);

        Client client = new Client(httpClient);

        Optional<JSONObject> json = client.call(mockedRequestParamsBuilder);
        assertTrue(json.isEmpty());
    }

    @Test
    void returnsAnEmptyOptionalUponInvalidStatus() throws IOException, InterruptedException {
        when(httpClient.send(any(), any(HttpResponse.BodyHandler.class))).thenReturn(mockedResponse);
        when(mockedResponse.statusCode()).thenReturn(404);

        Client client = new Client(httpClient);

        Optional<JSONObject> json = client.call(mockedRequestParamsBuilder);
        assertTrue(json.isEmpty());
    }
}
