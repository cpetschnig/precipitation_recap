package dev.cpetschnig.precipitation_1st.open_meteo;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestClient extends Mockito {

    @Test
    void returnsBodyOfApiCall() throws IOException, InterruptedException {
        HttpClient httpClient = mock(HttpClient.class);
        HttpResponse<String> response = mock(HttpResponse.class);

        when(httpClient.send(any(), any(HttpResponse.BodyHandler.class))).thenReturn(response);
        when(response.body()).thenReturn("Hello World");

        Client client = new Client(httpClient);
        assertTrue(client.call());
    }

    @Test
    void returnsFalseUponError() throws IOException, InterruptedException {
        HttpClient httpClient = mock(HttpClient.class);

        when(httpClient.send(any(), any(HttpResponse.BodyHandler.class))).thenThrow(java.net.ConnectException.class);

        Client client = new Client(httpClient);
        assertFalse(client.call());
    }
}
