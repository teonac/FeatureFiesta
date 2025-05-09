package com.example.app.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;

public class ReadPersonSteps {

    private String authenticatedUser;
    private String personId;
    private String bsn;
    private String anpId;
    private int responseStatusCode;
    private Map<String, Object> responseBody;

    private static final Map<String, Map<String, Object>> mockDatabase = new HashMap<>();

    static {
        // Mock database initialization
        mockDatabase.put("123e4567-e89b-12d3-a456-426614174000", Map.of(
                "id", "123e4567-e89b-12d3-a456-426614174000",
                "source", "BRP",
                "bsn", "123456789",
                "lastName", "Johnson",
                "initials", "A.B.",
                "email", "ajohnson@example.com"
        ));
        mockDatabase.put("ANP123456", Map.of(
                "id", "223e4567-e89b-12d3-a456-426614174001",
                "source", "ANP",
                "bsn", null,
                "anpId", "ANP123456",
                "lastName", "Smith"
        ));
    }

    @Given("the user is authenticated with normal access rights")
    public void theUserIsAuthenticatedWithNormalAccessRights() {
        authenticatedUser = "normal-user";
    }

    @When("the user reads a person with ID {string}")
    public void theUserReadsAPersonWithID(String id) {
        this.personId = id;
        if (mockDatabase.containsKey(id)) {
            responseStatusCode = 200;
            responseBody = mockDatabase.get(id);
        } else {
            responseStatusCode = 404;
            responseBody = Map.of("error", "Person not found");
        }
    }

    @When("the user reads a person with BSN {string}")
    public void theUserReadsAPersonWithBSN(String bsn) {
        this.bsn = bsn;
        responseBody = mockDatabase.values().stream()
                .filter(person -> bsn.equals(person.get("bsn")))
                .findFirst()
                .orElse(Map.of("error", "Person not found"));
        responseStatusCode = responseBody.containsKey("error") ? 404 : 200;
    }

    @When("the user reads a person with ANP_ID {string}")
    public void theUserReadsAPersonWithANP_ID(String anpId) {
        this.anpId = anpId;
        responseBody = mockDatabase.values().stream()
                .filter(person -> anpId.equals(person.get("anpId")))
                .findFirst()
                .orElse(Map.of("error", "Person not found"));
        responseStatusCode = responseBody.containsKey("error") ? 404 : 200;
    }

    @Then("the user receives status code {int}")
    public void theUserReceivesStatusCode(int expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
    }

    @Then("the user receives a person with the following details:")
    public void theUserReceivesAPersonWithTheFollowingDetails(Map<String, String> expectedDetails) {
        for (Map.Entry<String, String> entry : expectedDetails.entrySet()) {
            Assert.assertEquals(entry.getValue(), responseBody.get(entry.getKey()).toString());
        }
    }

    @Then("the user receives an error message {string}")
    public void theUserReceivesAnErrorMessage(String expectedErrorMessage) {
        Assert.assertEquals(expectedErrorMessage, responseBody.get("error"));
    }
}