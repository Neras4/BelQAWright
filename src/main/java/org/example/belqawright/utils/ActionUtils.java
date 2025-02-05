package org.example.belqawright.utils;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;

public class ActionUtils {
    public static void performActionWithScreenshot(Page page, String description, Runnable action, Locator elementLocator) {
        try {
            LoggingUtils.logInfo(description);
            LoggingUtils.logDebug(String.format("Performing action: %s", description));

            WebTestUtils.waitForElementReady(elementLocator, description, 50000);

            elementLocator.scrollIntoViewIfNeeded();
            page.waitForTimeout(100);

            highlightElement(elementLocator);

            ScreenshotUtils.takeScreenshot(page,  CommonUtils.generateUniqueName(description) + "_before");
            action.run();

            removeHighlight(elementLocator);

            LoggingUtils.logDebug(String.format("Action performed: %s", description));
        } catch (Exception e) {
            LoggingUtils.logError(String.format("Error during action: %s", description), e);
            throw new RuntimeException(String.format("Failed action: %s", description), e);
        }
    }

    public static void dragCircleWithDeltaAndScreenshot(Page page, Locator circleElement, Double deltaX, Double deltaY) {
        try {
            LoggingUtils.logInfo("Started drag circle element");
            LoggingUtils.logDebug("Waiting for the circle element to be ready");
            WebTestUtils.waitForElementReady(circleElement, "Circle element", 8000);

            BoundingBox box = circleElement.boundingBox();
            if (box == null) {
                throw new RuntimeException("Bounding box is null, circle may be hidden");
            }

            double cx = box.x + (box.width / 2);
            double cy = box.y + (box.height / 2);
            double newCx = cx + deltaX;
            double newCy = cy + deltaY;

            LoggingUtils.logDebug(String.format("Dragging circle from cx=%f, cy=%f to newCx=%f, newCy=%f", cx, cy, newCx, newCy));

            circleElement.scrollIntoViewIfNeeded();
            page.waitForTimeout(100);

            highlightElement(circleElement);
            ScreenshotUtils.takeScreenshot(page, CommonUtils.generateUniqueName("before_drag"));

            page.waitForTimeout(500);
            page.mouse().move(cx, cy);
            page.mouse().down();
            page.mouse().move(newCx, newCy, new Mouse.MoveOptions().setSteps(10));
            ScreenshotUtils.takeScreenshot(page, CommonUtils.generateUniqueName("during_drag"));
            page.mouse().up();

            ScreenshotUtils.takeScreenshot(page, CommonUtils.generateUniqueName("after_drag"));
            removeHighlight(circleElement);

            LoggingUtils.logDebug("Circle dragged successfully.");
        } catch (Exception e) {
            LoggingUtils.logError("Error during circle drag", e);
            throw new RuntimeException("Failed to drag circle", e);
        }
    }


    private static void highlightElement(Locator element) {
        if(element.isVisible()) element.evaluate("el => el.style.border = '5px solid red'");
    }

    private static void removeHighlight(Locator element) {
        if(element.isVisible()) element.evaluate("el => el.style.border = 'none'");
    }
}
