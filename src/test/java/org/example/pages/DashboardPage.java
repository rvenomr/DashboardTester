package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage extends BasePage {
    private String dashboardGreeetingXpath = "//span[contains(text(),'Home')]";

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dashboardGreeetingXpath)));
    }

    public String getStatusLabel() {
        return driver.findElement(By.xpath("//img[@alt = 'logo']/following-sibling::div/span"))
                .getAttribute("class");
    }
}
