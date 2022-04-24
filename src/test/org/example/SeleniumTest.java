package org.example;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.DashboardPage;
import org.example.pages.LoginPage;
import org.example.pages.DashboardsPage;
import org.example.pages.ResetPasswordPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public class SeleniumTest {
    private WebDriver driver;
    private String email;
    private String password;
    private String wrongEmail;

    @BeforeMethod
    public void startDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://chat1.site/login");
        driver.manage().window().maximize();
        try (InputStream input = new FileInputStream("src/test/resources/config.properties")) {

            Properties prop = new Properties();
            prop.load(input);
            email = prop.getProperty("email");
            password = prop.getProperty("password");
            wrongEmail = prop.getProperty("wrongEmail");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void statusTest() {
        try {
            LoginPage loginPage = new LoginPage(driver);
            DashboardsPage dashboardsPage = loginPage.login(email, password);
            String dashboardName = "Partner";
            String expectedStatus = "Invisible";
            dashboardsPage.setStatusLabelForDashboard(dashboardName, expectedStatus);
            String statusOfD = dashboardsPage.getStatusLabelForDashboard(dashboardName);
            Assert.assertTrue(statusOfD.contains(expectedStatus), "Actual value status of dashboard " + dashboardName + ": " + statusOfD.split(" ")[1]);
            String dashboardsPageWindowHandle = driver.getWindowHandle();
            String urlOfDashboardsPage = driver.getCurrentUrl();
            DashboardPage partnerDashboard = dashboardsPage.clickOnDashboard(dashboardName);
            String url = driver.getCurrentUrl();
            Assert.assertEquals(url, "https://" + dashboardName.toLowerCase(Locale.ROOT) + "." + urlOfDashboardsPage.substring(8).split("/")[0] + "/home");
            String actualStatus = partnerDashboard.getStatusLabel();
            Assert.assertEquals(actualStatus, expectedStatus);
            driver.close();
            driver.switchTo().window(dashboardsPageWindowHandle);

            expectedStatus = "Online";
            checkStatusTransitionByChoosingDashboard(dashboardsPage, dashboardName, expectedStatus);
            driver.close();
            driver.switchTo().window(dashboardsPageWindowHandle);

            expectedStatus = "Away";
            checkStatusTransitionByChoosingDashboard(dashboardsPage, dashboardName, expectedStatus);
            driver.switchTo().window(dashboardsPageWindowHandle);

            String dashboardWindowHandle = driver.getWindowHandles().stream().filter(h -> !h.equals(dashboardsPageWindowHandle)).findFirst().get();

            expectedStatus = "Online";
            checkStatusTransitionByChoosingDashboard(dashboardsPage, dashboardName, expectedStatus);
            driver.switchTo().window(dashboardWindowHandle);
            DashboardPage previouslyOpenedDashboard = new DashboardPage(driver);
            actualStatus = previouslyOpenedDashboard.getStatusLabel();
            Assert.assertEquals(actualStatus, expectedStatus, "When we change status of dashboard through dashboards page and when open it: previously opened tab of this dashboard didn't change it status");

            expectedStatus = "Away";
            driver.switchTo().window(dashboardsPageWindowHandle);
            dashboardsPage.setStatusLabelForDashboard(dashboardName, expectedStatus);
            statusOfD = dashboardsPage.getStatusLabelForDashboard(dashboardName);
            Assert.assertTrue(statusOfD.contains(expectedStatus), "Actual value status of dashboard " + dashboardName + ": " + statusOfD.split(" ")[1]);
            driver.switchTo().window(dashboardWindowHandle);
            actualStatus = previouslyOpenedDashboard.getStatusLabel();
            Assert.assertEquals(actualStatus, expectedStatus, "When we change status of dashboard through dashboards page and when switch to previously opened tab with this dashboard: status didn't changed");
        } catch (Exception e) {
            Assert.assertTrue(false, e.getMessage() + e.getStackTrace());
        }
    }

    private void checkStatusTransitionByChoosingDashboard(DashboardsPage dashboardsPage, String dashboardName, String expectedStatus) {
        try {
            dashboardsPage.setStatusLabelForDashboard(dashboardName, expectedStatus);
            String statusOfD = dashboardsPage.getStatusLabelForDashboard(dashboardName);
            Assert.assertTrue(statusOfD.contains(expectedStatus), "Actual value status of dashboard " + dashboardName + ": " + statusOfD.split(" ")[1]);
            DashboardPage dashboard = dashboardsPage.clickOnDashboard(dashboardName);
            String actualStatus = dashboard.getStatusLabel();
            Assert.assertEquals(actualStatus, expectedStatus);
        } catch (Exception e) {
            Assert.assertTrue(false, e.getMessage());
        }
    }

    @Test
    public void loginTestWrongPassword() {
        try {
            LoginPage loginPage = new LoginPage(driver);
            loginPage.setEmailField(email);
            loginPage.setPasswordField("12345");
            loginPage.pushLoginButton();
            Assert.assertTrue(loginPage.loginErrorIsDisplayed(), "Login error message not displayed");
            Assert.assertEquals(loginPage.getLoginErrorText(), "Login failed, wrong user credentials.");
        } catch (Exception e) {
            Assert.assertTrue(false, e.getMessage());
        }
    }

    @Test
    public void loginTestWrongEmail() {
        try {
            LoginPage loginPage = new LoginPage(driver);
            loginPage.pushLoginButton();
            Assert.assertTrue(loginPage.loginErrorIsDisplayed(), "Login error message not displayed");
            Assert.assertEquals(loginPage.getLoginErrorText(), "Login failed, wrong user credentials.");

            loginPage.setEmailField(wrongEmail);
            loginPage.setPasswordField(password);
            loginPage.pushLoginButton();
            Assert.assertTrue(loginPage.loginErrorIsDisplayed(), "Login error message not displayed");
            Assert.assertEquals(loginPage.getLoginErrorText(), "Login failed, wrong user credentials.");
        } catch (Exception e) {
            Assert.assertTrue(false, e.getMessage());
        }
    }

    @Test
    public void resetPassword() {
        try {
            LoginPage loginPage = new LoginPage(driver);
            ResetPasswordPage resetPasswordPage = loginPage.resetPassword();

            resetPasswordPage.pushSubmitButton();
            Assert.assertTrue(resetPasswordPage.isErrorMessageVisible(), "Reset password error message is not displayed");
            Assert.assertEquals(resetPasswordPage.getErrorMessageText(), "The email address is badly formatted.");

            resetPasswordPage.setEmailField("rossinskyrodion");
            resetPasswordPage.pushSubmitButton();
            Assert.assertTrue(resetPasswordPage.isErrorMessageVisible(), "Reset password error message is not displayed");
            Assert.assertEquals(resetPasswordPage.getErrorMessageText(), "The email address is badly formatted.");


            resetPasswordPage.setEmailField("@");
            resetPasswordPage.pushSubmitButton();
            Assert.assertTrue(resetPasswordPage.isErrorMessageVisible(), "Reset password error message is not displayed");
            Assert.assertEquals(resetPasswordPage.getErrorMessageText(), "The email address is badly formatted.");


            resetPasswordPage.setEmailField("gmail");
            resetPasswordPage.pushSubmitButton();
            Assert.assertTrue(resetPasswordPage.isErrorMessageVisible(), "Reset password error message is not displayed");
            Assert.assertEquals(resetPasswordPage.getErrorMessageText(), "The email address is badly formatted.");


            resetPasswordPage.setEmailField(".com");
            resetPasswordPage.pushSubmitButton();
            Assert.assertTrue(resetPasswordPage.isErrorMessageVisible(), "Reset password error message is not displayed");
            Assert.assertTrue(resetPasswordPage.isUserNotFoundMessageVisible());

            resetPasswordPage.pushBackButton();
        } catch (Exception e) {
            Assert.assertTrue(false, e.getMessage());
        }
    }

    @Test
    public void logoutTest() {
        try {
            LoginPage loginPage = new LoginPage(driver);
            DashboardsPage dashboardsPage = loginPage.login(email, password);
            dashboardsPage.pushLogoutButton();
            Thread.sleep(3000);
            boolean flag = dashboardsPage.isDashboardGreatingVisible();
            Assert.assertFalse(flag, "Logout button not working - dashboards greating is visible");
            flag = dashboardsPage.isAnyDashboardVisible();
            Assert.assertFalse(flag, "Logout button not working - some of dashboards are still visible");

            flag = loginPage.isLoginGreatingVisible();
            Assert.assertTrue(flag, "Logout button not working - dashboards greating is visible");
            flag = loginPage.isEmailFieldVisible();
            Assert.assertTrue(flag, "Email input field is not visible: something with logout");
            flag = loginPage.isPasswordFieldVisible();
            Assert.assertTrue(flag, "Password input field is not visible: something with logout");
        } catch (Exception e) {
            Assert.assertTrue(false, e.getMessage());
        }
    }

    @AfterMethod
    public void closeDriver() {
        driver.quit();
    }
}