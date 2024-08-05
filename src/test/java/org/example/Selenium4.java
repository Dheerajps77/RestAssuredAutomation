package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.openqa.selenium.devtools.v85.network.model.Request;
import org.openqa.selenium.devtools.v85.network.model.Response;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Selenium4 {

    public WebDriver driver;
    public final static int TIMEOUT = 10;
    Proxy proxy;
    ChromeOptions chromeOptions;


    @Test(enabled = false)
    public void setup2() {
        WebDriverManager.chromedriver().setup();
        chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TIMEOUT));
        driver.manage().window().maximize();
        driver.get("https://www.google.com");
        System.out.println("First tab title: " + driver.getTitle());

        String parentWindow = driver.getWindowHandle();
        System.out.println("Parent window" + parentWindow);
        driver.switchTo().newWindow(WindowType.TAB);

        // Navigate to another website in the new tab
        driver.get("https://www.bing.com");

        // Perform some actions in the new tab
        System.out.println("Title of the new tab: " + driver.getTitle());

        // Close the new tab
        driver.close();

        driver.switchTo().window(parentWindow);
        driver.switchTo().newWindow(WindowType.WINDOW);
    }

    @Test(enabled = true)
    public void JavaScriptNewWindowExample() {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        try {
            // Navigate to a URL
            driver.get("https://www.google.com");
            System.out.println("First window title: " + driver.getTitle());
            String parentWindow = driver.getWindowHandle();

            // Use JavaScript to open a new window
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.open('https://www.bing.com', '_blank');");


            System.out.println("New window title: " + driver.getTitle());

            driver.switchTo().newWindow(WindowType.TAB);
            driver.get("https://www.makemytrip.com/");

            System.out.println("New window title: " + driver.getTitle());


            driver.switchTo().newWindow(WindowType.TAB);
            driver.get("https://toolsqa.com/selenium-webdriver/actions-class-in-selenium/");

            driver.switchTo().newWindow(WindowType.TAB);
            driver.get("https://www.browserstack.com/guide/selenium-3-vs-selenium-4");

            Set<String> windows = driver.getWindowHandles();

            List<String> arrayList = new ArrayList<String>(windows);
            int totalSizeofOpenWindow = arrayList.size();

            for (int i = 1; i < totalSizeofOpenWindow; i++) {
                String childWindow = arrayList.get(i);
                if (!parentWindow.equalsIgnoreCase(childWindow)) {
                    driver.switchTo().window(arrayList.get(i));
                    System.out.println("New window title: " + driver.getTitle());
                    driver.close();
                }
            }

        driver.findElement(By.xpath(""));
        } finally {
            driver.quit();
        }
    }
}
