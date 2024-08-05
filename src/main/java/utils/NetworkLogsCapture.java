package utils;

import java.time.Duration;
import java.util.Base64;
import java.util.Optional;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.network.Network.GetResponseBodyResponse;
import org.openqa.selenium.devtools.v85.network.model.Request;
import org.openqa.selenium.devtools.v85.network.model.RequestId;
import org.openqa.selenium.devtools.v85.network.model.Response;

import io.github.bonigarcia.wdm.WebDriverManager;

public class NetworkLogsCapture {

    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions chromeOptions = new ChromeOptions();
        WebDriver driver = new ChromeDriver(chromeOptions);

        try {
            DevTools devTools = ((ChromeDriver) driver).getDevTools();
            devTools.createSession();

            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();

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

            devTools.addListener(Network.responseReceived(), event -> {
                Response response = event.getResponse();
                System.out.println("Response URL: " + response.getUrl());
                System.out.println("Response Status: " + response.getStatus());
                System.out.println("Response Headers: " + response.getHeaders());

                // Capture response body
                RequestId requestId = event.getRequestId();
                try {
                    GetResponseBodyResponse responseBody = devTools.send(Network.getResponseBody(requestId));
                    if (responseBody != null) {
                        String body = responseBody.getBody();
                        if (responseBody.getBase64Encoded()) {
                            byte[] decodedBytes = Base64.getDecoder().decode(body);
                            String contentType = response.getMimeType();
                            if (contentType.startsWith("text") || contentType.endsWith("json") || contentType.endsWith("xml")) {
                                body = new String(decodedBytes);
                            } else {
                                body = "Binary Data (" + decodedBytes.length + " bytes)";
                            }
                        }
                        System.out.println("Response Body: " + body);
                    } else {
                        System.out.println("No response body received.");
                    }
                } catch (Exception e) {
                    System.err.println("Error while fetching response body: " + e.getMessage());
                }

                System.out.println("------------------------------------");
            });

            driver.get("https://www.snapdeal.com/products/men-apparel-sports-wear?sort=plrty");

            // Wait for a while to ensure network events are captured
            Thread.sleep(10000); // Adjust as needed

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }
}
