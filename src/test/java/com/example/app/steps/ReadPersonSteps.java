package com.example.app.steps;

import com.example.app.model.Person;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
public class ReadPersonSteps {

    private static final DateTimeFormatter DOB_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private int responseStatusCode;
    private Map<String, Object> responseBody;
    private List<Person> personList = new ArrayList<>();
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

    @And("the following persons exist in the system:")
    public void theFollowingPersonsExistInTheSystem(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);

        for (Map<String, String> row : rows) {
            Person person = new Person();
            person.setId(UUID.fromString(row.get("id")));
            person.setSource(row.get("source"));
            person.setBsn(row.get("bsn"));
            person.setAnpId(row.get("anp_id"));
            person.setLastName(row.get("last_name"));
            person.setLastNamePrefix(row.get("last_name_prefix"));
            person.setInitials(row.get("initials"));
            person.setGenderDesignation(row.get("gender_designation"));
            person.setDateOfBirth(!checkNull(row.get("date_of_birth")) ? LocalDate.parse(row.get("date_of_birth"), DOB_FORMATTER) : null);
            person.setAuthorizedPersonId(!checkNull(row.get("authorized_person_id")) ? UUID.fromString(row.get("authorized_person_id")) : null);
            person.setEmail(row.get("email"));
            person.setIndicationSecret(Integer.parseInt(row.get("indication_secret")));

            personList.add(person);
        }
    }

    private boolean checkNull(String value) {
        return value == null || value.equals("null");
    }

    private Map<String, Object> personToMap(Person p) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", p.getId().toString());
        map.put("source", p.getSource());
        map.put("bsn", p.getBsn());
        map.put("anpId", p.getAnpId());
        map.put("lastName", p.getLastName());
        map.put("lastNamePrefix", p.getLastNamePrefix());
        map.put("initials", p.getInitials());
        map.put("genderDesignation", p.getGenderDesignation());
        map.put("dateOfBirth", p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : null);
        map.put("authorizedPersonId", p.getAuthorizedPersonId() != null ? p.getAuthorizedPersonId().toString() : null);
        map.put("email", p.getEmail());
        map.put("indicationSecret", p.getIndicationSecret());
        return map;
    }

    @Given("the API is available")
    public void theAPIIsAvailable() {
        // Stub pentru API disponibil
    }

  @Given("the user is authenticated with normal access rights")
  public void theUserIsAuthenticatedWithNormalAccessRights() {
        loadUser("CIVIL_SERVANT");
    }



    @When("the user reads a person with ID {string}")
    public void theUserReadsAPersonWithID(String id) {
        if (currentUser == null) {
            responseStatusCode = 401;
            responseBody = Map.of("error", "Authentication required");
            return;
        }
        if (!currentUser.getPermissions().contains("READ_PERSON")) {
            responseStatusCode = 403;
            responseBody = Map.of("error", "Insufficient permissions to access this person's details");
            return;
        }

        responseBody = personList.stream()
                .filter(p -> p.getId().toString().equals(id))
                .findFirst()
                .map(this::personToMap)
                .orElse(Map.of("error", "Person not found"));

        responseStatusCode = responseBody.containsKey("error") ? 404 : 200;

    }

    @When("the user reads a person with BSN {string}")
    public void theUserReadsAPersonWithBSN(String bsn) {
        responseBody = personList.stream()
                .filter(p -> bsn.equals(p.getBsn()))
                .findFirst()
                .map(this::personToMap)
                .orElse(Map.of("error", "Person not found"));
        responseStatusCode = responseBody.containsKey("error") ? 404 : 200;
    }

    @When("the user reads a person with ANP_ID {string}")
    public void theUserReadsAPersonWithANP_ID(String anpId) {
        responseBody = personList.stream()
                .filter(p -> anpId.equals(p.getAnpId()))
                .findFirst()
                .map(this::personToMap)
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
            Assert.assertEquals(entry.getValue(), Objects.toString(responseBody.get(entry.getKey()), null));
        }
    }

    @Then("the user receives an error message {string}")
    public void theUserReceivesAnErrorMessage(String expectedErrorMessage) {
        Assert.assertEquals(expectedErrorMessage, responseBody.get("error"));
    }

    // Neimplementateee
    @When("the user reads a person without providing BSN or ANP_ID")
    public void theUserReadsAPersonWithoutProvidingBSNOrANP_ID() {}

    @When("the user reads a person with BSN {string} and ANP_ID {string}")
    public void theUserReadsAPersonWithBSNAndANP_ID(String arg0, String arg1) {}

    @When("the user creates a person with email {string}")
    public void theUserCreatesAPersonWithEmail(String arg0) {}

    @When("the user reads a person with ID {string} without authentication")
    public void theUserReadsAPersonWithIDWithoutAuthentication(String arg0) {
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
    public void theResponseTimeShouldBeLessThanMilliseconds(int arg0) {}

    @And("the system should log the attempted injection")
    public void theSystemShouldLogTheAttemptedInjection() {}

    @And("the response content type should be {string}")
    public void theResponseContentTypeShouldBe(String arg0) {}

    @When("the user sends {int} concurrent requests to read different persons")
    public void theUserSendsConcurrentRequestsToReadDifferentPersons(int arg0) {}

    @Then("all responses should be received within {int} seconds")
    public void allResponsesShouldBeReceivedWithinSeconds(int arg0) {}

    @And("at least {int}% of responses should have status code {int}")
    public void atLeastOfResponsesShouldHaveStatusCode(int arg0, int arg1) {}

    @Given("the user is authenticated with admin access rights")
    public void theUserIsAuthenticatedWithAdminAccessRights() {
        loadUser("ADMIN");
    }

    @Given("the user is authenticated with user access rights")
    public void theUserIsAuthenticatedWithUserAccessRights() {
        loadUser("USER");
    }
}
