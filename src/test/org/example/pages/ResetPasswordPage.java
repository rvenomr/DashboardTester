package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ResetPasswordPage extends BasePage{
    String resetPasswordTitle = "//h2[text()='Reset Password']";
    String userNotFoundMessage = "There is no user record corresponding to this identifier. The user may have been deleted.";

    @FindBy(xpath = "//div[@class='Error']")
    private WebElement errorMessage;

    @FindBy(how = How.XPATH, using = "//input[@name='email']")
    private WebElement emailField;

    @FindBy(how = How.XPATH, using = "//button[./span[text()='Submit']]")
    private WebElement submitButton;

    @FindBy(how = How.XPATH, using = "//button[./span[text()='Back']]")
    private WebElement backButton;

    public ResetPasswordPage(WebDriver driver){
        init(driver,10, resetPasswordTitle);
        PageFactory.initElements(driver, this);
    }

    public void pushSubmitButton(){
        submitButton.click();
    }

    public LoginPage pushBackButton(){
        backButton.click();
        return new LoginPage(driver);
    }

    public boolean isErrorMessageVisible(){
        return errorMessage.isDisplayed();
    }

    public boolean isUserNotFoundMessageVisible(){
        wait.until(ExpectedConditions.textToBePresentInElement(errorMessage,userNotFoundMessage));
        return errorMessage.isDisplayed();
    }

    public String getErrorMessageText(){
        return errorMessage.getText();
    }

    public void setEmailField(String email) {
        setField(emailField, email);
    }

}
