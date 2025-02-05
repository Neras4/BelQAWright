package org.example.belqawright.utils;

import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import com.microsoft.playwright.Page;

public class LoggingUtils {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(LoggingUtils.class);

    public static void logInfo(String message) {
        logger.info(message);
        Allure.addAttachment("INFO: " + message, "text/plain", message);
    }

    public static void logError(String description, Throwable e) {
        String errorMessage = String.format("%s: %s", description, e.getMessage());
        logger.error(errorMessage,  e);
        Allure.addAttachment("ERROR: " + description, "text/plain", errorMessage);
    }

    public static void logInfoWithScreenshot(Page page, String description) {
        try {
            String uniqueName =  CommonUtils.generateUniqueName(description);
            logInfo(description);
            logDebug(description);

            ScreenshotUtils.takeScreenshot(page, uniqueName);
        } catch (Exception e) {
            logError("Failed to log info with screenshot", e);
        }
    }

    public static void logWarn(String message) {
        logger.warn(message);
        Allure.addAttachment("WARN:" + message, "text/plain", message);
    }

    public static void logDebug(String message) {
        logger.debug(message);
    }
}
