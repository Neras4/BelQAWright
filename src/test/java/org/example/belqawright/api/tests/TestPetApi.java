package org.example.belqawright.api.tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.belqawright.api.utils.ApiTestBase;
import org.example.belqawright.api.utils.PetFactory;
import org.example.belqawright.api.utils.ValidatorJson;
import org.example.belqawright.api.utils.PetApiHelper;
import org.example.belqawright.constants.TestApiConstants;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

@Epic("API Tests")
@Feature("Pet API Tests")
public class TestPetApi extends ApiTestBase {
    private final PetApiHelper petApi = new PetApiHelper();
    private final int petId = new Random().nextInt(100000000);
    private final int petCategoryId = new Random().nextInt(100000);
    private String jsonBodyForNewPet = PetFactory.buildPetJson(petId, petCategoryId,
            "google", 25, "available");
    private String jsonBodyForUpdatePet = PetFactory.updatePetJson(jsonBodyForNewPet, 26,
            "babka", "sold");

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = TestApiConstants.BASE_API_URL;
    }

    @Test(description = "Test: Create a new pet", priority = 1)
    @Story("Pet API")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateNewPet() {
        petApi.createPetAndValidate(jsonBodyForNewPet, 200, null, true);
    }

    @Test(description = "Test: create a pet with missing body", priority = 2)
    @Story("Pet API")
    @Severity(SeverityLevel.NORMAL)
    public void testCreatePetWithMissingName() {
        petApi.createPetAndValidate("", 405, "no data", false);
    }

    @Test(description = "Test: get a pet by correct ID", priority = 3)
    @Story("Pet API")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPetById() {
        petApi.getPetByIdAndValidate(petId, 200, jsonBodyForNewPet, null);
    }

    @Test(description = "Test: get a pet by not existed ID", priority = 4)
    @Story("Pet API")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetByNotExistedId() {
        int incorrectPetId = new Random().nextInt(100000);
        petApi.getPetByIdAndValidate(incorrectPetId, 404, null, "Pet not found");
    }

    @Test(description = "Test: Get pets by all valid statuses", priority = 5)
    @Story("Pet API")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPetsByValidStatuses() {
        List.of("available", "pending", "sold").forEach(status ->
                petApi.getPetsByStatusAndValidate(status, false)
        );
    }

    @Test(description = "Test: Get pets by incorrect status", priority = 6)
    @Story("Pet API")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPetsByIncorrectStatus() {
        petApi.getPetsByStatusAndValidate("test", true);
    }

    @Test(description = "Test: Update existing pet", priority = 7)
    @Story("Pet API")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdateExistingPet() {
        Response response = petApi.updatePetAndValidate(jsonBodyForUpdatePet, 200, null);
        ValidatorJson.validateJsonEquals(jsonBodyForUpdatePet, response.asString());
    }

    @Test(description = "Test: Update pet with incorrect ID", priority = 8)
    @Story("Pet API")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdatePetWithIncorrectId() {
        String incorrectJsonBody = """
                {"id": "-k"}
                """;

        petApi.updatePetAndValidate(incorrectJsonBody, 500, "something bad happened");
    }

    @Test(description = "Test: Update existing pet with form data", priority = 9)
    @Story("Pet API")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdateExistingPetWithFormData() {
        petApi.updatePetWithFormData(petId, 200, "google", null, null);
    }

    @Test(description = "Test: Update existing pet form data with incorrect ID", priority = 10)
    @Story("Pet API")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateExistingPetWithFormDataWithIncorrectId() {
        int incorrectPetId = new Random().nextInt(100000);
        petApi.updatePetWithFormData(incorrectPetId, 404, "nanago", "sold", "not found");
    }

    @Test(description = "Test: Delete a pet", priority = 11)
    @Story("Pet API")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeletePet() {
        petApi.deletePetAndValidate(petId, 200, String.valueOf(petId));
    }

    @Test(description = "Test: Delete a pet with incorrect ID", priority = 12)
    @Story("Pet API")
    @Severity(SeverityLevel.NORMAL)
    public void testDeletePetWithIncorrectId() {
        int incorrectPetId = new Random().nextInt(100000);
        petApi.deletePetAndValidate(incorrectPetId, 404, null);
    }

    @Test(description = "Test: Delete a pet with incorrect ID as not numeric value", priority = 13)
    @Story("Pet API")
    @Severity(SeverityLevel.NORMAL)
    public void testDeletePetWithIncorrectIdAsNotNumeric() {
        petApi.deletePetAndValidate("k", 404, "java.lang.NumberFormatException: For input string: \"k\"");
    }
}