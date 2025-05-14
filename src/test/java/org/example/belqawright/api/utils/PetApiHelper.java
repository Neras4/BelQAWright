package org.example.belqawright.api.utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.example.belqawright.constants.TestApiConstants;

import static org.hamcrest.Matchers.equalTo;

public class PetApiHelper extends ApiTestBase {
    public void createPetAndValidate(String jsonBody, int expectedStatusCode, String expectedMessage, boolean validateBody) {
        ValidatableResponse validatable = baseRequest()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .post(TestApiConstants.PET_API)
                .then()
                .statusCode(expectedStatusCode);

        if (expectedMessage != null) {
            validatable.body("message", equalTo(expectedMessage));
        }

        Response response = validatable.extract().response();

        if (validateBody) {
            ValidatorJson.validateJsonEquals(jsonBody, response.asString());
        }
    }

    public void getPetByIdAndValidate(Object petId, int expectedStatusCode, String expectedBody, String expectedMessage) {
        ValidatableResponse validatable = baseRequest()
                .contentType(ContentType.JSON)
                .pathParam("petId", petId)
                .when()
                .get(TestApiConstants.PET_API + "/{petId}")
                .then()
                .statusCode(expectedStatusCode);

        if (expectedMessage != null) {
            validatable.body("message", equalTo(expectedMessage));
        }

        Response response = validatable.extract().response();

        if (expectedBody != null) {
            ValidatorJson.validateJsonEquals(expectedBody, response.asString());
        }
    }

    public void getPetsByStatusAndValidate(String status, boolean shouldBeEmpty) {
        Response response = executeApiCall("Get pets by status: " + status, () ->
                baseRequest()
                        .contentType(ContentType.JSON)
                        .queryParam("status", status)
                        .when()
                        .get(TestApiConstants.FIND_BY_STATUS)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response()
        );

        if (shouldBeEmpty) {
            ValidatorJson.validateIfResponseBodyIsEmpty(response);
        } else {
            ValidatorJson.validateJsonFields(response, status);
        }
    }

    public Response updatePetAndValidate(String jsonBody, int expectedStatusCode, String expectedMessage) {
        ValidatableResponse validatable = baseRequest()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .put(TestApiConstants.PET_API)
                .then()
                .statusCode(expectedStatusCode);

        if (expectedMessage != null) {
            validatable.body("message", equalTo(expectedMessage));
        }

        return validatable.extract().response();
    }

    public void updatePetWithFormData(Object petId, int expectedStatusCode, String name, String status, String expectedMessage) {
        RequestSpecification request = baseRequest()
                .contentType("application/x-www-form-urlencoded")
                .pathParam("petId", petId)
                .queryParam("name", name);

        if (status != null) {
            request = request.queryParam("status", status);
        }

        ValidatableResponse validatable = request
                .when()
                .post(TestApiConstants.PET_API + "/{petId}")
                .then()
                .statusCode(expectedStatusCode);

        if (expectedMessage != null) {
            validatable.body("message", equalTo(expectedMessage));
        }
    }

    public void deletePetAndValidate(Object petId, int expectedStatusCode, String expectedMessage) {
        ValidatableResponse validatable =
                baseRequest()
                        .contentType(ContentType.JSON)
                        .pathParam("petId", petId)
                        .when()
                        .delete(TestApiConstants.PET_API + "/{petId}")
                        .then()
                        .statusCode(expectedStatusCode);

        if (expectedMessage != null) {
            validatable.body("message", equalTo(expectedMessage));
        }
    }
}
