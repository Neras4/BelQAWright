package org.example.belqawright.ui.validation;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.example.belqawright.utils.WebTestUtils;
import org.testng.Assert;

public class ValidatorLogin {
    public static void validateErrorMessageIsPresent (Page page, String message) {
        Locator warning = page.locator("p.caption.status-danger").getByText(message);
        page.locator("h1:text('Login')").click();
        Assert.assertTrue(WebTestUtils.waitForElementVisible(warning, 300),
                String.format("'%s' is not visible", message));
    }

    public static void validateErrorMessageIsNotPresent (Page page, String message) {
        Locator warning = page.locator("p.caption.status-danger").getByText(message);
        page.locator("h1:text('Login')").click();
        Assert.assertTrue(WebTestUtils.waitForElementNotVisible(warning, 300),
                String.format("'%s' is visible", message));
    }

    public static void validateLoginButtonIsActive (Page page, boolean isActive) {
        Locator loginButton = page.locator("button", new Page.LocatorOptions().setHasText("Log In"));
        WebTestUtils.waitForElementVisible(loginButton, 1000);
        String loginButtonState = loginButton.getAttribute("aria-disabled");

        if (isActive) {
            Assert.assertEquals(loginButtonState, "false", "Login In button is not active");
        } else {
            Assert.assertEquals(loginButtonState, "true", "Login In button is active");
        }
    }
}
