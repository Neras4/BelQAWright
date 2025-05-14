package org.example.belqawright.api.utils;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.example.belqawright.utils.CustomRequestLoggingFilter;
import org.example.belqawright.utils.TestLoggingUtils;

import static io.restassured.RestAssured.given;

public abstract class ApiTestBase {
    protected Response executeApiCall(String stepDescription, ApiStep step) {
        TestLoggingUtils.logInfo(stepDescription);
        try {
            Response response = step.execute();
            TestLoggingUtils.logResponseBody(response);
            return response;
        } catch (Exception e) {
            TestLoggingUtils.logError("Error during step: " + stepDescription, e);
            throw new RuntimeException("Failed during API call: " + stepDescription, e);
        }
    }

    protected RequestSpecification baseRequest() {
        return given()
                .filter(new CustomRequestLoggingFilter());
    }

    @FunctionalInterface
    public interface ApiStep {
        Response execute();
    }
}
