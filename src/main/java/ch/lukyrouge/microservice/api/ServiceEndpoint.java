package ch.lukyrouge.microservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static ch.lukyrouge.microservice.Microservice.MS;

public class ServiceEndpoint {
    private final String endpoint;
    private final String host;

    public ServiceEndpoint(String endpoint, String host) {
        this.endpoint = endpoint;
        this.host = host;
    }

    public ServiceEndpoint(String endpoint) {
        this.endpoint = endpoint;
        this.host = MS.getConfig().getString("host");
    }

    public <T> T get(String apiUrl, Class<T> responseType) throws IOException {
        return get(apiUrl, responseType, new Query());
    }

    public  <T> T get(String apiUrl, Class<T> responseType, Query query) throws IOException {
        StringBuilder URLString = new StringBuilder(host);
        if (!endpoint.isEmpty()) URLString.append("/").append(endpoint);
        if (!apiUrl.isEmpty()) URLString.append("/").append(apiUrl);
        URL url = new URL(URLString.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {

            return connectionToObject(connection, responseType);
        }
        throw new IOException("Error making API request, response code: " + responseCode);
    }

    private static <T> T connectionToObject(HttpURLConnection connection, Class<T> type) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parse the JSON response to create an instance of the generic class
        String jsonResponse = response.toString();
        T result = parseJsonToClass(jsonResponse, type);

        // Close the connection
        connection.disconnect();
        return result;
    }

    private static <T> T parseJsonToClass(String json, Class<T> clazz) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new IOException("Error deserializing JSON", e);
        }
    }
}
