package org.example.belqawright.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.testng.Assert;

public class WebTestUtils {
    public static void navigateToURL(Page page, String url, int timeout) {
        try {
            LoggingUtils.logInfo("Navigated to page and accepted cookies.");

            page.navigate(url, new Page.NavigateOptions().setTimeout(timeout));
            page.waitForTimeout(500);
        } catch (Exception e) {
            LoggingUtils.logError("Error during page navigation", e);
            throw new RuntimeException("Failed to navigate to page", e);
        }
    }

    public static void clearCookies(Page page) {
        try {
            LoggingUtils.logInfo("Cleared cookies after test.");

            page.context().clearCookies();
        } catch (Exception e) {
            LoggingUtils.logError("Error clearing cookies", e);
        }
    }

    public static void waitForElementReady(Locator elementLocator, String actionName, int timeout) {
        String normalizedAction = actionName.trim().toLowerCase();

        if(!waitForElementVisible(elementLocator, timeout)) {
            throw new RuntimeException(String.format("Element not visible for action: %s", actionName));
        }

        if(normalizedAction.contains("click") && !elementLocator.isEnabled()) {
            throw new RuntimeException(String.format("Element not enabled for action: %s", actionName));
        }

        if((normalizedAction.contains("fill") || normalizedAction.contains("type")) && !elementLocator.isEditable()) {
            throw new RuntimeException(String.format("Element not editable for action: %s", actionName));
        }

        LoggingUtils.logDebug(String.format("Element is ready for action: %s", actionName));
    }

    public static void verifyNavigationAndHeaderVisibility(Page page, int timeoutMs, String expectedUrl, Locator headerLocator, String errorMessage) {
        validateElementVisibility(headerLocator, timeoutMs, errorMessage);
        Assert.assertEquals(page.url(), expectedUrl, "Failed to navigate. Current URL is: " + page.url());
    }

    public static void validateElementVisibility(Locator element, int timeoutMs, String errorMessage) {
        boolean isVisible = waitForElementVisible(element, timeoutMs);
        Assert.assertTrue(isVisible, errorMessage);
    }

    public static boolean waitForElementVisible(Locator locator, int timeout) {
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(timeout));
            boolean isVisible = locator.isVisible();

            if (!isVisible) LoggingUtils.logError("Element is found, but not visible", new Throwable());
            return isVisible;
        } catch (Exception e) {
            LoggingUtils.logError("Error waiting for element to be visible " + e.getMessage(), e);
            return false;
        }
    }

    public static boolean waitForElementNotVisible(Locator locator, int timeout) {
        try {
            locator.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.HIDDEN)
                    .setTimeout(timeout));
            boolean isVisible = locator.isVisible();

            if (isVisible) LoggingUtils.logError("Element is found, but not hidden", new Throwable());
            return !isVisible;
        } catch (Exception e) {
            LoggingUtils.logError("Error waiting for element to be not visible " + e.getMessage(), e);
            return false;
        }
    }
}
