package org.example.belqawright.validation;

import com.microsoft.playwright.Page;
import org.example.belqawright.utils.LoggingUtils;
import org.testng.Assert;

import java.util.List;

public class EssentialUIValidator {
    public static void validateEssentialUIElements(Page page, List<UIElement> uiElements) {
        StringBuilder errorMessages = new StringBuilder();
        for (UIElement element : uiElements) {
            if (!validateElementPresenceWithResult(page, element.selector())) {
                errorMessages.append(element.errorMessage()).append("\n");
            }
        }
        if (!errorMessages.isEmpty()) {
            LoggingUtils.logError("Validation failed for essential UI elements:\n" + errorMessages,
                    new Exception("Validation error"));
            Assert.fail("Validation failed for the following UI elements:\n" + errorMessages);
        }
    }

    private static boolean validateElementPresenceWithResult(Page page, String selector) {
        if (!page.locator(selector).isVisible()) {
            LoggingUtils.logError("Element not found: " + selector,
                    new Exception("UI Element not visible: " + selector));
            return false;
        }
        LoggingUtils.logInfo("Validated presence of element: " + selector);
        return true;
    }

}