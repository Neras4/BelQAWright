package org.example.belqawright.pages.auth;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.example.belqawright.utils.ActionUtils;
import org.example.belqawright.utils.LoggingUtils;
import org.example.belqawright.validation.UIElement;

import java.util.ArrayList;
import java.util.List;

public class Login {
    private final Page page;

    public Login(Page page) {
        this.page = page;
    }

    @Step("Complete login")
    public void fillAllLoginFields(String email, String password) {
        fillEmail(email);
        fillPassword(password);
    }

    @Step("Fill email")
    public void fillEmail(String email) {
        fillLoginField("email", email);
    }

    @Step("Fill password")
    public void fillPassword(String password) {
        fillLoginField("password", password);
    }

    @Step("Click checkbox")
    public void clickCheckbox(boolean rememberMe) {
        try {
            LoggingUtils.logInfoWithScreenshot(page, "Click checkbox");
            Locator checkbox = page.locator("nb-checkbox[name='rememberMe'] span.custom-checkbox");

            if (rememberMe && !checkbox.isChecked()) {
                ActionUtils.performActionWithScreenshot(page, "Check checkbox", checkbox::check, checkbox);
            } else {
                ActionUtils.performActionWithScreenshot(page, "Uncheck checkbox", checkbox::uncheck, checkbox);
            }
        } catch (Exception e) {
            LoggingUtils.logError("Error during click checkbox", e);
            throw new RuntimeException("Failed to click checkbox", e);
        }
    }

   @Step("Click login button")
    public void clickLoginButton() {
        try {
            LoggingUtils.logInfoWithScreenshot(page, "Click login button");
            Locator loginButton = page.locator("button", new Page.LocatorOptions().setHasText("Log In"));
            ActionUtils.performActionWithScreenshot(page, "Click login button", loginButton::click, loginButton);
        } catch (Exception e) {
            LoggingUtils.logError("Error during click login button", e);
            throw new RuntimeException("Failed to click login button", e);
        }
    }

    @Step("Go to register page")
    public Register goToRegisterPage() {
        try {
            LoggingUtils.logInfoWithScreenshot(page, "Go to register page");
            Locator registerLink = page.locator("a", new Page.LocatorOptions().setHasText("Register"));
            ActionUtils.performActionWithScreenshot(page, "Click register link", registerLink::click, registerLink);

            return new Register(page);
        } catch (Exception e) {
            LoggingUtils.logError("Error during go to register page", e);
            throw new RuntimeException("Failed to go to register page", e);
        }
    }

    public List<UIElement> getEssentialUIElementsOnLogin() {
        List<UIElement> elements = new ArrayList<>();
        elements.add(new UIElement("form.ng-pristine",
                "Login form could not be located on the page"));
        elements.add(new UIElement("section[aria-label='Register']",
                "Link to Register form could not be located on the page"));

        return elements;
    }

    private void fillLoginField(String inputName, String value) {
        try {
            LoggingUtils.logInfoWithScreenshot(page, String.format("Fill field '%s' with value '%s'", inputName, value));
            Locator registerField = page.locator("input[name='%s']".formatted(inputName));
            ActionUtils.performActionWithScreenshot(page, String.format("Filling field '%s' with value '%s'", inputName, value),
                    () -> registerField.fill(value), registerField);
        } catch (Exception e) {
            LoggingUtils.logError(String.format("Error during fill field '%s' with value '%s'", inputName, value), e);
            throw new RuntimeException(String.format("Failed fill field '%s' with value '%s'", inputName, value), e);
        }
    }
}
