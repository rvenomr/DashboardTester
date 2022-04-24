package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    public WebDriver driver;
    public WebDriverWait wait;
    public String baseElementPath;

    public void init(WebDriver driver, int durationOfWaitingOfBaseElement, String baseElementPath) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(durationOfWaitingOfBaseElement));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(baseElementPath)));
    }

    public void setField(WebElement element, String text) {
        element.click();
        element.clear();
        element.sendKeys(text);
    }
}