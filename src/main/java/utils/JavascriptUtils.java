package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class JavascriptUtils {
    private WebDriver driver;
    private JavascriptExecutor js;

    public JavascriptUtils(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
    }

    // Scroll to an element
    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView(true);", element);


        WebElement ele = driver.findElement(By.xpath(""));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOf(ele));
    }

    public WebElement expandRootElement(WebElement element) {
        WebElement ele = (WebElement) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].shadowRoot", element);
        return ele;
    }

    // Click an element
    public void clickElement(WebElement element) {
        js.executeScript("arguments[0].click();", element);
    }

    // Get the title of the page
    public String getPageTitle() {
        return js.executeScript("return document.title;").toString();
    }

    // Get the inner text of an element
    public String getElementText(WebElement element) {
        return js.executeScript("return arguments[0].innerText;", element).toString();
    }

    // Set the value of an input field
    public void setInputValue(WebElement element, String value) {
        js.executeScript("arguments[0].value='" + value + "';", element);
    }

    // Highlight an element
    public void highlightElement(WebElement element) {
        js.executeScript("arguments[0].style.border='3px solid red'", element);
    }

    // Refresh the page
    public void refreshPage() {
        js.executeScript("history.go(0)");
    }

    // Get the URL of the page
    public String getPageURL() {
        return js.executeScript("return document.URL;").toString();
    }

    // Scroll to the bottom of the page
    public void scrollToBottom() {
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    // Scroll to the top of the page
    public void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }
}