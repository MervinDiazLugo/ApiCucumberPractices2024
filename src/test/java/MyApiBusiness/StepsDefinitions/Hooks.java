package MyApiBusiness.StepsDefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.java.Log;

@Log
public class Hooks {

  @Before("@ApiTesting")
  public void initWebDriver(Scenario scenario) {
    log.info("*********************************************************");
    log.info("[ Configuration ] - Initializing API test configuration");
    log.info("*********************************************************");

    log.info("*********************************************************");
    log.info("[ Scenario ] - " + scenario.getName());
    log.info("*********************************************************");
  }
}
