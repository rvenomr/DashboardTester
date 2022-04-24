package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage extends BasePage {
    public String baseElementPath = "//h2[text()='Welcome back']";

    @FindBy(how = How.XPATH, using = "//input[@name='email']")
    private WebElement emailField;

    @FindBy(how = How.XPATH, using = "//input[@name='password']")
    private WebElement passwordField;

    @FindBy(how = How.XPATH, using = "//button[./span[contains(text(),'Login')]]")
    private WebElement loginButton;

    @FindBy(xpath = "//div[@class='Error']")
    private WebElement loginError;

    @FindBy(xpath = "//button[./span[text()='FORGOT YOUR PASSWORD?']]")
    private WebElement resetPassword;

    public LoginPage(WebDriver driver) {
        init(driver, 10, baseElementPath);
        PageFactory.initElements(driver, this);
    }

    public DashboardsPage login(String email, String password) {
        setField(emailField, email);
        setField(passwordField, password);
        loginButton.click();
        return new DashboardsPage(driver);
    }

    public void setEmailField(String email) {
        setField(emailField, email);
    }

    public void setPasswordField(String password) {
        setField(passwordField, password);
    }

    public void pushLoginButton() {
        loginButton.click();
    }

    public boolean loginErrorIsDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(9));
        wait.until(ExpectedConditions.visibilityOf(loginError));
        return loginError.isDisplayed();
    }

    public ResetPasswordPage resetPassword() {
        resetPassword.click();
        return new ResetPasswordPage(driver);
    }

    public String getLoginErrorText() {
        return loginError.getText();
    }

    public boolean isEmailFieldVisible() {
        return emailField.isDisplayed();
    }

    public boolean isPasswordFieldVisible() {
        return passwordField.isDisplayed();
    }

    public boolean isLoginGreatingVisible() {
        return passwordField.isDisplayed();
    }
}
