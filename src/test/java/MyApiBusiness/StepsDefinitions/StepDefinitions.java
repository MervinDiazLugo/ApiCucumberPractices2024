package MyApiBusiness.StepsDefinitions;

import config.api.RestAssuredExtension;
import io.cucumber.java.en.*;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@Log
public class StepDefinitions extends RestAssuredExtension {

    @Given("an example scenario")
    public void anExampleScenario() {
    }

    @When("all step definitions are implemented")
    public void allStepDefinitionsAreImplemented() {
    }

    @Then("the scenario passes")
    public void theScenarioPasses() {
    }

    @Given("^I do a GET in (.*?)$")
    public void iDoAGETInPetFindByStatusStatusAvailable(String endpoint) {
        apiGet(endpoint);
    }

    @Given("^I print the api Response$")
    public void iDoAPrint() {
        response.getBody().prettyPrint();
    }

    @And("^I validate status code is (.*?)$")
    public void iValidateStatusCodeIs(String code) {
        assertStatusCode(Integer.parseInt(code));
    }

    @Given("^I do a POST in (.*?) using body (.*?)$")
    public void iDoAPost(String endpoint, String bodyPath) {
        apiPost(endpoint, bodyPath);
    }

    @Then("^I save the response key (.*?) as (.*?)$")
    public void iSaveTheResponseKeys(String responseKey, String newKey) {
        saveInTestData(newKey, retrieveJsonPathResponse(responseKey));
        //saveInTestData(newKey, retrieveResponse(responseKey));
    }

    @Given("^I do a PUT in (.*?) using body (.*?)$")
    public void iDoAPut(String endpoint, String bodyPath, List<List<String>> table) {
        getDataFromTable(table);
        apiPut(endpoint, bodyPath);
    }

    @Given("^I do a DELETE on (.*?)$")
    public void iDoADelete(String endpoint) {
        apiDelete(endpoint);
    }

    @And("^I assert entity (.*?) is (.*?)$")
    public void iAssertEntityMessageIs(String key, String value) {
        assertKeyMessages(key, value);
    }

    @And("^I assert Response values$")
    public void iAssertResponseValues(List<List<String>> table) {
        assertResponseFromTable(table);
    }
}
