package org.example.belqawright.pages.auth;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;
import org.example.belqawright.utils.ActionUtils;
import org.example.belqawright.utils.LoggingUtils;
import org.example.belqawright.validation.UIElement;

import java.util.ArrayList;
import java.util.List;

public class Register {
    private final Page page;

    public Register(Page page) {
        this.page = page;
    }

    @Step("Complete registration")
    public void fillAllRegistrationFields(String fullName, String email, String password) {
        fillFullName(fullName);
        fillEmail(email);
        fillPassword(password);
        fillRepeatPassword(password);
    }

    @Step("Fill full name")
    public void fillFullName(String fullName) {
        fillRegisterField("fullName", fullName);
    }

    @Step("Fill email")
    public void fillEmail(String email) {
        fillRegisterField("email", email);
    }

    @Step("Fill password")
    public void fillPassword(String password) {
        fillRegisterField("password", password);
    }

    @Step("Fill repeat password")
    public void fillRepeatPassword(String repeatPassword) {
        fillRegisterField("rePass", repeatPassword);
    }

    @Step("Click checkbox")
    public void clickCheckbox(boolean isAgreeWithTerms) {
        try {
            LoggingUtils.logInfoWithScreenshot(page, "Click checkbox");
            Locator checkbox = page.locator("nb-checkbox[name='terms'] span.custom-checkbox");

            if (isAgreeWithTerms && !checkbox.isChecked()) {
                ActionUtils.performActionWithScreenshot(page, "Check checkbox", checkbox::check, checkbox);
            } else  {
                ActionUtils.performActionWithScreenshot(page, "Uncheck checkbox", checkbox::uncheck, checkbox);
            }
        } catch (Exception e) {
            LoggingUtils.logError("Error during click checkbox", e);
            throw new RuntimeException("Failed to click checkbox", e);
        }
    }

    @Step("Click register button")
    public void clickRegisterButton() {
        try {
            LoggingUtils.logInfoWithScreenshot(page, "Click register button");
            Locator registerButton = page.locator("button", new Page.LocatorOptions().setHasText("Register"));
            ActionUtils.performActionWithScreenshot(page, "Click register button", registerButton::click, registerButton);
        } catch (Exception e) {
            LoggingUtils.logError("Error during click register button", e);
            throw new RuntimeException("Failed to click register button", e);
        }
    }

    @Step("Go to login page")
    public Login goToLoginPage() {
        try {
            LoggingUtils.logInfoWithScreenshot(page, "Go to login page");
            Locator loginLink = page.locator("a", new Page.LocatorOptions().setHasText("Log in"));
            ActionUtils.performActionWithScreenshot(page, "Click login link", loginLink::click, loginLink);

            return new Login(page);
        } catch (Exception e) {
            LoggingUtils.logError("Error during go to login page", e);
            throw new RuntimeException("Failed to go to login page", e);
        }
    }

    public List<UIElement> getEssentialUIElementsOnRegister() {
        List<UIElement> elements = new ArrayList<>();
        elements.add(new UIElement("form.ng-pristine",
                "Register form could not be located on the page"));
        elements.add(new UIElement("section[aria-label='Sign in']",
                "Link to Login form could not be located on the page"));

        return elements;
    }

    private void fillRegisterField(String inputName, String value) {
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
