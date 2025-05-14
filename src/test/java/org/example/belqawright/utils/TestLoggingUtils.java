package org.example.belqawright.utils;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

public class TestLoggingUtils {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(TestLoggingUtils.class);

    public static void logResponseBody(Response response) {
        StringBuilder log = new StringBuilder();
        log.append("==== RESPONSE ====\n")
                .append("Status Code: ").append(response.getStatusCode()).append("\n")
                .append("Status Line: ").append(response.getStatusLine()).append("\n")
                .append("Time (ms): ").append(response.getTime()).append("\n")
                .append("Headers: ").append(response.getHeaders()).append("\n\n")
                .append("Body:\n").append(response.getBody().asPrettyString());

        String logText = log.toString();
        logDebug(logText);
        Allure.addAttachment("API Response", "text/plain", logText);
    }

    public static void logError(String context, Throwable e) {
        String message = context + ": " + e.getMessage();
        logger.error(message, e);
        Allure.addAttachment("API ERROR", "text/plain", message);
    }

    public static void logInfo(String message) {
        logger.info(message);
        Allure.addAttachment("INFO: " + message, "text/plain", message);
    }

    public static void logDebug(String message) {
        logger.debug(message);
    }

    public static void logRequestDetails(String method, String uri, Map<String, String> headers, String body) {
        StringBuilder log = new StringBuilder();
        log.append("==== REQUEST ====\n")
                .append(method).append(" ").append(uri).append("\n");

        if (headers != null && !headers.isEmpty()) {
            log.append("Headers:\n");
            headers.forEach((key, value) ->
                    log.append(key).append("=").append(value).append("\n"));
        }

        if (body != null && !body.isBlank()) {
            log.append("\nBody:\n").append(body).append("\n");
        }

        String logText = log.toString();
        logDebug(logText);
        Allure.addAttachment("API Request", "text/plain", logText);
    }

}
