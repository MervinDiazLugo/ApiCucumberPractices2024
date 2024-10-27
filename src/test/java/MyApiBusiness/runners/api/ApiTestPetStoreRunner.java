package MyApiBusiness.runners.api;

import config.api.RestAssuredConfigProperties;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import lombok.extern.java.Log;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

@Log
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
public class ApiTestPetStoreRunner extends AbstractTestNGCucumberTests {
  @BeforeTest
  @Parameters({"api.env", "api.client", "api.version"})
  public void beforeSuite(
      @Optional("null") String environment,
      @Optional("null") String client,
      @Optional("null") String version) {
    log.info("TestNG api.env for this test set is " + environment);
    log.info("TestNG api.client for this test set is " + client);
    log.info("TestNG api.version for this test set is " + version);
    RestAssuredConfigProperties.setTestNgEnvironment(environment);
    RestAssuredConfigProperties.setTestNgClient(client);
    RestAssuredConfigProperties.setTestNgVersion(version);
  }
}
