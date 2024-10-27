package MyApiBusiness.runners.api;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    tags = "@ApiTesting and not @Ignore",
    features = "src/test/resources/MyApiBusiness/PetStoreApiTest",
    glue = "MyApiBusiness.StepsDefinitions",
    plugin = {
      "pretty",
      "summary",
      "html:test-output",
      "json:target/cucumber/cucumber.json",
      "html:target/cucumber-html-report.html"
    })
public class ApiTestPetStoreRunnerNoParams extends AbstractTestNGCucumberTests {}
