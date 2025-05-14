package org.example.belqawright.api.utils;

import io.restassured.response.Response;
import org.example.belqawright.utils.TestLoggingUtils;
import org.skyscreamer.jsonassert.JSONAssert;
import org.json.JSONException;
import org.testng.Assert;

import java.util.List;

public class ValidatorJson {
    public static void validateJsonEquals(String expected, String actual) {
        try {
            JSONAssert.assertEquals(expected, actual, false);
        } catch (JSONException e) {
            TestLoggingUtils.logError("JSON comparison failed", e);
            throw new RuntimeException("JSON comparison failed", e);
        }
    }

    public static void validateJsonFields(Response response, String expectedValue) {
        try {
            List<String> statuses = response.jsonPath().getList("status");

            for (int i = 0; i < statuses.size(); i++) {
                String actualValue = statuses.get(i);
                Assert.assertEquals(actualValue, expectedValue,
                        String.format("Element #%d has incorrect field value: expected='%s', actual='%s'",
                                i, expectedValue, actualValue));
            }
        } catch (Exception e) {
            TestLoggingUtils.logError("Failed to assert field in JSON", e);
            throw new RuntimeException("Error while validating field fields in JSON response", e);
        }
    }

    public static void validateIfResponseBodyIsEmpty(Response response) {
        try {
            List<?> list = response.jsonPath().getList("");
            Assert.assertTrue(list.isEmpty(), "Expected empty response array, but got: " + list);
        } catch (Exception e) {
            TestLoggingUtils.logError("Failed to assert empty response body", e);
            throw new RuntimeException("Error while validating empty response body", e);
        }

    }
}
