package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import java.util.Locale;
import java.util.Set;

public class DashboardsPage extends BasePage {
    private final String dashboardsLoadedXpath = "//div[@role='button']";

    @FindBy(how = How.XPATH, using = "//h2[text()=\"Dashboards\"]")
    private WebElement dashboards;

    @FindBy(xpath = "//button[./span[contains(text(),'Logout')]]")
    private WebElement logoutButton;

    public DashboardsPage(WebDriver driver) {
        init(driver, 15, dashboardsLoadedXpath);
        PageFactory.initElements(driver, this);
    }

    public String getStatusLabelForDashboard(String dashboardName) {
        return driver.findElement(By.xpath("//div[@role='button' and ./div[text()='" + dashboardName.toLowerCase(Locale.ROOT) + "']]//span[contains(@class,'jss18')]")).getAttribute("class");
    }

    public DashboardPage clickOnDashboard(String name) {
        Set<String> windowHandlesBeforeClick = driver.getWindowHandles();
        driver.findElement(By.xpath("//div[text()='" + name.toLowerCase(Locale.ROOT) + "']")).click();
        Set<String> windowHandlesAfterClick = driver.getWindowHandles();
        windowHandlesAfterClick.removeAll(windowHandlesBeforeClick);
        String dashboard = windowHandlesAfterClick.stream().findFirst().get();
        driver.switchTo().window(dashboard);
        return new DashboardPage(driver);
    }

    public void setStatusLabelForDashboard(String dashboardName, String status) {
        dashboardName = dashboardName.toLowerCase(Locale.ROOT);
        driver.findElement(By.xpath("//div[@role='button' and ./div[text()='" + dashboardName + "']]//button")).click();
        driver.findElement(By.xpath("//button[./span[text()='" + status + "']]")).click();
    }

    public LoginPage pushLogoutButton() {
        logoutButton.click();
        return new LoginPage(driver);
    }

    public boolean isDashboardGreatingVisible() {
        return dashboards.isDisplayed();
    }

    public boolean isAnyDashboardVisible() {
        return driver.findElement(By.xpath(dashboardsLoadedXpath)).isDisplayed();
    }
}
