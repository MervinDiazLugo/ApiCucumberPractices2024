package MyApiBusiness;

import config.api.RestAssuredConfigProperties;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

@Log
public class testConfig {

  @Test
  public void ConfigTest() {
    RestAssuredConfigProperties racp = new RestAssuredConfigProperties();

    log.info(racp.getBaseUri());
    Assert.assertTrue(
        "petstore is not in the string",
        StringUtils.containsIgnoreCase(racp.getBaseUri(), "petstore.swagger"));
  }
}
