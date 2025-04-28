package org.example.belqawright.validation.ST;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.example.belqawright.utils.WebTestUtils;
import org.testng.Assert;

public class ValidatorRegister {
    public static void validateErrorMessageIsPresent (Page page, String message) {
        Locator warning = page.locator("p.caption.status-danger").getByText(message);
        page.locator("h1:text('Register')").click();
        Assert.assertTrue(WebTestUtils.waitForElementVisible(warning, 300),
                String.format("'%s' is not visible", message));
    }

    public static void validateErrorMessageIsNotPresent (Page page, String message) {
        Locator warning = page.locator("p.caption.status-danger").getByText(message);
        page.locator("h1:text('Register')").click();
        Assert.assertTrue(WebTestUtils.waitForElementNotVisible(warning, 300),
                String.format("'%s' is visible", message));
    }

    public static void validateRegisterButtonIsActive (Page page, boolean isActive) {
        Locator registerButton = page.locator("button", new Page.LocatorOptions().setHasText("Register"));
        WebTestUtils.waitForElementVisible(registerButton, 1000);
        String registerButtonState = registerButton.getAttribute("aria-disabled");

        if (isActive) {
            Assert.assertEquals(registerButtonState, "false", "Register button is not active");
        } else {
            Assert.assertEquals(registerButtonState, "true", "Register button is active");
        }
    }

    public static void validateConfirmationPassword (Page page, boolean isPassed) {
        String confirmPasswordStatus = page.locator("input[name='rePass']").getAttribute("ng-reflect-status");

        if (isPassed) {
            Assert.assertEquals(confirmPasswordStatus, "success", "Confirmation password is not passed");
        } else {
            Assert.assertEquals(confirmPasswordStatus, "danger", "Confirmation password is passed");
        }
    }
}
