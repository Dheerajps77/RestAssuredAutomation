package org.example;

import java.util.Optional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.network.Network.GetResponseBodyResponse;
import org.openqa.selenium.devtools.v85.network.model.Request;
import org.openqa.selenium.devtools.v85.network.model.RequestId;
import org.openqa.selenium.devtools.v85.network.model.Response;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.v85.page.Page;
import org.openqa.selenium.devtools.v85.page.model.Frame;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.github.bonigarcia.wdm.WebDriverManager;

public class ToPrintEventDataFromNetworkTab {

	public static void main(String[] args) {
        // Setup WebDriverManager and initialize WebDriver
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            // Navigate to the target URL
            driver.get("https://www.snapdeal.com/products/men-apparel-sports-wear?sort=plrty");

            // Get DevTools and create session
            DevTools devTools = ((ChromeDriver) driver).getDevTools();
            devTools.createSession();

            // Enable Network domain
            devTools.send(Network.enable(Optional.of(100000), Optional.empty(), Optional.empty()));

            // Add a listener for network responses
            devTools.addListener(Network.responseReceived(), responseReceived -> {
                RequestId requestId = responseReceived.getRequestId();
                System.out.println("Response URL: " + responseReceived.getResponse().getUrl());

                GetResponseBodyResponse response=devTools.send(Network.getResponseBody(requestId));
                // Capture the response body
               /* devTools.send(Network.getResponseBody(requestId)). thenAccept(responseBody -> {*/
                    String body = response.getBody();
                    if (response.getBase64Encoded()) {
                        byte[] decodedBytes = java.util.Base64.getDecoder().decode(body);
                        body = new String(decodedBytes);
                    }
                    System.out.println("Response Body: " + body);
                });

            // Add a listener for data received
            devTools.addListener(Network.dataReceived(), dataReceived -> {
               /// byte[] data = dataReceived.getData();
                String payload = new String(""); // Convert byte array to string
                System.out.println("Payload Data: " + payload);
            });

            // Let the browser load and gather network data
            Thread.sleep(10000); // Adjust this as needed

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}