package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.*;
import utils.ExcelUtils;

import java.io.IOException;

public class AdvancedDataProviderExample {

    WebDriver driver;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() throws IOException {
        return ExcelUtils.getExcelData("src/test/resources/testdata/CreateAirlineData.xlsx", "Sheet3");
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password) {
        driver.get("https://example.com/login");

        WebElement usernameField = driver.findElement(By.id("username"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));

        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        loginButton.click();

        // Add assertions to verify the login functionality
    }

    @DataProvider(name = "searchData")
    public Object[][] searchDataProvider() {
        return new Object[][]{
                {"Selenium"},
                {"TestNG"},
                {"DataProvider"}
        };
    }

    @Test(dataProvider = "searchData")
    public void testSearch(String query) {
        driver.get("https://example.com");

        WebElement searchField = driver.findElement(By.id("search"));
        WebElement searchButton = driver.findElement(By.id("searchButton"));

        searchField.sendKeys(query);
        searchButton.click();

        // Add assertions to verify the search functionality
    }
}