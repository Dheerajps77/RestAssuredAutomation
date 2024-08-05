package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.network.model.Request;
import org.openqa.selenium.devtools.v85.network.model.Response;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Optional;

public class BaseTest {

    public WebDriver driver;
    public final static int TIMEOUT = 10;
    Proxy proxy;
    ChromeOptions chromeOptions;

    public WebDriver WebDriverManager() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
            driver.manage().window().maximize();
            driver.get("https://qaautomation.expert/2024/04/19/parallel-testing-in-cucumber-with-testng/");
        }
        return driver;
    }

    @Test
    public void setup() {

        WebDriverManager.chromedriver().setup();
        proxy = new Proxy();
        chromeOptions = new ChromeOptions();

        driver = new ChromeDriver(chromeOptions);

        // Initialize DevTools
        DevTools devTools = ((ChromeDriver) driver).getDevTools();

        devTools.createSession();

        // Enable network logging
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        driver.manage().window().maximize();

        // Print network events
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
            System.out.println("------------------------------------");
        });

        driver.get("https://www.snapdeal.com/products/men-apparel-sports-wear?sort=plrty");
    }

    @Test(enabled = false)
    public void testChrome() {
        // Test case for Chrome browser
        driver.get("https://qaautomation.expert/2024/04/19/parallel-testing-in-cucumber-with-testng/");
        System.out.println("Title of the page in Chrome: " + driver.getTitle());
        // Add assertions or other test steps as needed
    }

    @Test(enabled = false)
    public void testFirefox() {
        // Setup Firefox WebDriver using WebDriverManager
        WebDriverManager.firefoxdriver().browserVersion("126").setup();
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        driver.manage().window().maximize();
        // Test case for Firefox browser
        driver.get("https://www.snapdeal.com/products/men-apparel-sports-wear?sort=plrty");
        System.out.println("Title of the page in Firefox: " + driver.getTitle());
        // Add assertions or other test steps as needed
    }

    @Test(enabled = false)
    public void testEdge() {
        // Setup Edge WebDriver using WebDriverManager
        //WebDriverManager.edgedriver().browserVersion("126.0.2592.87").setup();
        WebDriverManager.edgedriver().setup();
        driver = new EdgeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        driver.manage().window().maximize();
        // Test case for Edge browser
        driver.get("https://qaautomation.expert/2024/04/19/parallel-testing-in-cucumber-with-testng/");
        System.out.println("Title of the page in Edge: " + driver.getTitle());
        // Add assertions or other test steps as needed
    }

    //@AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

