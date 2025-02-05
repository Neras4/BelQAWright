package org.example.belqawright.utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtils {
    private static final String BASE_DIRECTORY = "target/allure-results/screenshots";

    public static void  takeScreenshot(Page page, String testName)  {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = String.format("%s_%s_screenshot.png", testName, timestamp);
        Path screenshotPath = Paths.get(BASE_DIRECTORY, fileName);

        try{
            LoggingUtils.logDebug("Taking screenshot for test: " + testName);

            Path directoryPath = screenshotPath.getParent();
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            page.screenshot(new Page.ScreenshotOptions().setPath(screenshotPath));

            byte[] screenshotBytes = Files.readAllBytes(screenshotPath);
            Allure.getLifecycle().addAttachment("Screenshot", "image/png", ".png", screenshotBytes);

            LoggingUtils.logDebug("Screenshot successfully captured for test: " + testName);
            LoggingUtils.logDebug("Screenshot path: " + screenshotPath);
        } catch (IOException e) {
            LoggingUtils.logError("IO Error during take a screenshot " + testName, e);
        }
    }
}