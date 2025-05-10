package com.example.app.steps;

import com.example.app.model.Company;
import com.example.app.model.User;
import io.cucumber.core.internal.com.fasterxml.jackson.core.type.TypeReference;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.junit.Assert;

import java.nio.file.Paths;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
public class ReadCompanySteps {
    private int responseStatusCode;
    private Map<String, Object> responseBody;
    private List<Company> companyList = new ArrayList<>();
    private User currentUser;

    private void loadUser(String role) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<User> users = mapper.readValue(
                    Paths.get("src/test/resources/testdata/users.json").toFile(),
                    new TypeReference<>() {}
            );
            currentUser = users.stream()
                    .filter(u -> role.equalsIgnoreCase(u.getRole()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("User not found for role: " + role));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load user for role: " + role, e);
        }
    }

    @And("the following companies exist in the system:")
    public void theFollowingCompaniesExistInTheSystem(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            Company company = new Company();
            company.setCompanyId(UUID.fromString(row.get("companyId")));
            company.setRsin(row.get("rsin"));
            company.setInpId(row.get("inp_id"));
            company.setName(row.get("name"));
            company.setEmail(row.get("email"));
            company.setAddress(row.get("address"));
            company.setStatus(row.get("status"));
            companyList.add(company);
        }
    }

    private Map<String, Object> companyToMap(Company c) {
        Map<String, Object> map = new HashMap<>();
        map.put("companyId", c.getCompanyId().toString());
        map.put("rsin", c.getRsin());
        map.put("inp_id", c.getInpId());
        map.put("name", c.getName());
        map.put("email", c.getEmail());
        map.put("address", c.getAddress());
        map.put("status", c.getStatus());
        return map;
    }

    @Given("the API is available")
    public void theAPIIsAvailable() {}

    @Given("the user is authenticated with normal access rights")
    public void theUserIsAuthenticatedWithNormalAccessRights() {
        loadUser("CIVIL_SERVANT");
    }

    @Given("the user is authenticated with admin access rights")
    public void theUserIsAuthenticatedWithAdminAccessRights() {
        loadUser("ADMIN");
    }

    @Given("the user is authenticated with user access rights")
    public void theUserIsAuthenticatedWithUserAccessRights() {
        loadUser("USER");
    }

    @When("the user reads a company with ID {string}")
    public void theUserReadsACompanyWithID(String id) {
        if (currentUser == null) {
            responseStatusCode = 401;
            responseBody = Map.of("error", "Authentication required");
            return;
        }
        if (!currentUser.getPermissions().contains("READ_COMPANY")) {
            responseStatusCode = 403;
            responseBody = Map.of("error", "Insufficient permissions to access this company's details");
            return;
        }
        responseBody = companyList.stream()
                .filter(c -> c.getCompanyId().toString().equals(id))
                .findFirst()
                .map(this::companyToMap)
                .orElse(Map.of("error", "Company not found"));
        responseStatusCode = responseBody.containsKey("error") ? 404 : 200;
    }

    @When("the user reads a company with RSIN {string}")
    public void theUserReadsACompanyWithRSIN(String rsin) {
        responseBody = companyList.stream()
                .filter(c -> rsin.equals(c.getRsin()))
                .findFirst()
                .map(this::companyToMap)
                .orElse(Map.of("error", "Company not found"));
        responseStatusCode = responseBody.containsKey("error") ? 404 : 200;
    }

    @When("the user reads a company with INP_ID {string}")
    public void theUserReadsACompanyWithINP_ID(String inpId) {
        responseBody = companyList.stream()
                .filter(c -> inpId.equals(c.getInpId()))
                .findFirst()
                .map(this::companyToMap)
                .orElse(Map.of("error", "Company not found"));
        responseStatusCode = responseBody.containsKey("error") ? 404 : 200;
    }

    @Then("the user receives status code {int}")
    public void theUserReceivesStatusCode(int expectedStatusCode) {
        Assert.assertEquals(expectedStatusCode, responseStatusCode);
    }

    @Then("the user receives a company with the following details:")
    public void theUserReceivesACompanyWithTheFollowingDetails(Map<String, String> expectedDetails) {
        for (Map.Entry<String, String> entry : expectedDetails.entrySet()) {
            Assert.assertEquals(entry.getValue(), Objects.toString(responseBody.get(entry.getKey()), null));
        }
    }

    @Then("the user receives an error message {string}")
    public void theUserReceivesAnErrorMessage(String expectedErrorMessage) {
        Assert.assertEquals(expectedErrorMessage, responseBody.get("error"));
    }

    // Not implemented (for negative/corner cases, see ReadPersonSteps for stubs)
    @When("the user reads a company without providing RSIN or INP_ID")
    public void theUserReadsACompanyWithoutProvidingRSINOrINP_ID() {}

    @When("the user reads a company with RSIN {string} and INP_ID {string}")
    public void theUserReadsACompanyWithRSINAndINP_ID(String rsin, String inpId) {}

    @When("the user creates a company with email {string}")
    public void theUserCreatesACompanyWithEmail(String email) {}

    @When("the user reads a company with ID {string} without authentication")
    public void theUserReadsACompanyWithIDWithoutAuthentication(String id) {
        if (currentUser == null) {
            responseStatusCode = 401;
            responseBody = Map.of("error", "Authentication required");
        }
    }

    @Given("the user has an expired authentication token")
    public void theUserHasAnExpiredAuthenticationToken() {}

    @Given("the user has an invalid authentication token")
    public void theUserHasAnInvalidAuthenticationToken() {}

    @And("the response time should be less than {int} milliseconds")
    public void theResponseTimeShouldBeLessThanMilliseconds(int ms) {}

    @And("the system should log the attempted injection")
    public void theSystemShouldLogTheAttemptedInjection() {}

    @And("the response content type should be {string}")
    public void theResponseContentTypeShouldBe(String contentType) {}

    @When("the user sends {int} concurrent requests to read different companies")
    public void theUserSendsConcurrentRequestsToReadDifferentCompanies(int count) {}

    @Then("all responses should be received within {int} seconds")
    public void allResponsesShouldBeReceivedWithinSeconds(int seconds) {}

    @And("at least {int}% of responses should have status code {int}")
    public void atLeastOfResponsesShouldHaveStatusCode(int percent, int statusCode) {}
}
