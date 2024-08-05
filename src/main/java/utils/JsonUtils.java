package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.network.model.Request;
import org.openqa.selenium.devtools.v85.network.model.Response;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> getJsonDataAsMap(String jsonFileName) throws IOException {
        String completeJsonFilePath = System.getProperty("user.dir") + "/src/test/resources/" + jsonFileName;
        Map<String, Object> data = objectMapper.readValue(new File(completeJsonFilePath), new TypeReference<>() {
        });
        return data;
    }

    // Method to setup network traffic logging
    public static void setupNetworkTrafficLogging(ChromeDriver driver) {
        ChromeOptions options = new ChromeOptions();
        DevTools devTools = driver.getDevTools();
        devTools.createSession();

        // Add listener for Request Will Be Sent event
        devTools.addListener(Network.requestWillBeSent(), event -> {
            Request request = event.getRequest();
            System.out.println("Request URL: " + request.getUrl());
            System.out.println("Request Method: " + request.getMethod());
            System.out.println("Request Headers: " + request.getHeaders());
            if (request.getPostData() != null) {
                System.out.println("Request Payload: " + request.getPostData());
            }
            System.out.println("------------------------------------");
        });

        // Add listener for Response Received event
        devTools.addListener(Network.responseReceived(), event -> {
            Response response = event.getResponse();
            System.out.println("Response URL: " + response.getUrl());
            System.out.println("Response Status: " + response.getStatus());
            System.out.println("Response Headers: " + response.getHeaders());
            System.out.println("------------------------------------");
        });
    }
}