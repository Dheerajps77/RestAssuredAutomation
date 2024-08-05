package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import utils.ExcelUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SeleniumExcelTest {

    public static void main(String[] args) throws IOException {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.get("http://example.com");

        ExcelUtils excelUtils = new ExcelUtils("path/to/excel.xlsx", "Sheet1");
        List<Map<String, String>> testData = excelUtils.getAllData();

        for (Map<String, String> row : testData) {
            String id = row.get("ID");
            String name = row.get("Name");
            String userName = row.get("UserName");

            WebElement idElement = driver.findElement(By.id("idInput"));
            WebElement nameElement = driver.findElement(By.id("nameInput"));
            WebElement userNameElement = driver.findElement(By.id("userNameInput"));

            driver.findElement(By.xpath("")).getShadowRoot();

            idElement.sendKeys(id);
            nameElement.sendKeys(name);
            userNameElement.sendKeys(userName);

            // Perform other actions and assertions as needed
        }

        excelUtils.closeWorkbook();
        driver.quit();
    }
}
