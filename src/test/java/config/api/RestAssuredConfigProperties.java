package config.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

@Log
public class RestAssuredConfigProperties extends RestAssuredHelper {
  private static Properties prop = new Properties();
  private static final String GLOBAL_DATA_FILE_LOCATION = "/ApiTest.properties";

  public static String BASE_URI,
      AUTH_ENDPOINT,
      API_SYSTEM_VERSION,
      API_PROPERTIES_VERSION,
      API_TESTNG_VERSION,
      API_VERSION,
      API_SYSTEM_ENVIRONMENT,
      API_PROPERTIES_ENVIRONMENT,
      API_TESTNG_ENVIRONMENT,
      API_ENVIRONMENT,
      API_SYSTEM_CLIENT,
      API_PROPERTIES_CLIENT,
      API_TESTNG_CLIENT,
      API_CLIENT,
      API_BODY_DATA,
      API_USER,
      API_PASSWORD,
      API_AUTH_ENDPOINT;

  public RestAssuredConfigProperties() {
    initConfig();
  }

  public void initConfig() {
    try {
      InputStream input =
          RestAssuredConfigProperties.class.getResourceAsStream(GLOBAL_DATA_FILE_LOCATION);
      prop.load(input);
    } catch (IOException e) {
      e.printStackTrace();
    }

    API_SYSTEM_ENVIRONMENT = System.getenv("api_environment");
    API_PROPERTIES_ENVIRONMENT = prop.getProperty("api.env");

    API_SYSTEM_CLIENT = System.getenv("api_client");
    API_PROPERTIES_CLIENT = prop.getProperty("api.client");

    API_SYSTEM_VERSION = System.getenv("api_version");
    API_PROPERTIES_VERSION = prop.getProperty("api.version");
    // API_PROPERTIES_CLIENT = prop.getProperty("webdriver.client");

    AUTH_ENDPOINT = prop.getProperty("assured.authenticationEndpoint");
    API_VERSION = prop.getProperty("api.version");
    API_BODY_DATA = prop.getProperty("api.bodyData");

    API_USER = prop.getProperty("api.username");
    API_PASSWORD = prop.getProperty("api.password");
    API_AUTH_ENDPOINT = prop.getProperty("api.authenticationEndpoint");
  }

  public String getBaseUri() {
    String urlProperty = String.format("api.%s.baseUri.%s", getEnvironment(), getClient());
    BASE_URI = prop.getProperty(urlProperty) != null ? prop.getProperty(urlProperty) : null;

    if (StringUtils.isEmpty(BASE_URI)) {
      urlProperty =
          String.format("api.%s.baseUri.%s", getPropertiesEnvironment(), getPropertiesClient());
      BASE_URI = prop.getProperty(urlProperty) != null ? prop.getProperty(urlProperty) : null;
    }
    Assert.assertTrue(StringUtils.isNotEmpty(BASE_URI), "url base malformation");

    return BASE_URI;
  }

  public String getEnvironment() {
    API_SYSTEM_ENVIRONMENT = getSystemEnvironment();
    API_TESTNG_ENVIRONMENT = getTestNgEnvironment();
    API_PROPERTIES_ENVIRONMENT = getPropertiesEnvironment();
    API_ENVIRONMENT =
        StringUtils.isNotEmpty(API_SYSTEM_ENVIRONMENT)
            ? API_SYSTEM_ENVIRONMENT
            : StringUtils.isNotEmpty(API_TESTNG_ENVIRONMENT)
                ? API_TESTNG_ENVIRONMENT
                : StringUtils.isNotEmpty(API_PROPERTIES_ENVIRONMENT)
                    ? API_PROPERTIES_ENVIRONMENT
                    : null;

    return API_ENVIRONMENT;
  }

  public String getSystemEnvironment() {
    return API_SYSTEM_ENVIRONMENT;
  }

  public String getSystemClient() {
    return API_SYSTEM_CLIENT;
  }

  public String getTestNgEnvironment() {
    return API_TESTNG_ENVIRONMENT;
  }

  public String getPropertiesEnvironment() {
    return API_PROPERTIES_ENVIRONMENT;
  }

  public String getClient() {
    API_SYSTEM_CLIENT = getSystemClient();
    API_TESTNG_CLIENT = getTestNgClient();
    API_PROPERTIES_CLIENT = getPropertiesClient();
    API_CLIENT =
        StringUtils.isNotEmpty(API_SYSTEM_CLIENT)
            ? API_SYSTEM_CLIENT
            : StringUtils.isNotEmpty(API_TESTNG_CLIENT)
                ? API_TESTNG_CLIENT
                : StringUtils.isNotEmpty(API_PROPERTIES_CLIENT) ? API_PROPERTIES_CLIENT : null;

    return API_CLIENT;
  }

  public static String getApiVersion() {
    API_SYSTEM_VERSION = getSystemVersion();
    API_TESTNG_VERSION = getTestNgVersion();
    API_PROPERTIES_VERSION = getPropertiesVersion();
    API_VERSION =
        StringUtils.isNotEmpty(API_SYSTEM_VERSION)
            ? API_SYSTEM_VERSION
            : StringUtils.isNotEmpty(API_TESTNG_VERSION)
                ? API_TESTNG_VERSION
                : StringUtils.isNotEmpty(API_PROPERTIES_VERSION) ? API_PROPERTIES_VERSION : null;

    return API_VERSION;
  }

  public static String getSystemVersion() {
    return API_SYSTEM_VERSION;
  }

  public static String getTestNgVersion() {
    return API_TESTNG_VERSION;
  }

  public static String getPropertiesVersion() {
    return API_PROPERTIES_VERSION;
  }

  public String getTestNgClient() {
    return API_TESTNG_CLIENT;
  }

  public String getPropertiesClient() {
    return API_PROPERTIES_CLIENT;
  }

  public static String getApiUser() {
    return API_USER;
  }

  public static String getApiPassword() {
    return API_PASSWORD;
  }

  public String getAuthenticationEndpoint() {
    String authEndpointProperty =
        String.format("api.%s.authenticationEndpoint.%s", getEnvironment(), getClient());

    API_AUTH_ENDPOINT =
        prop.getProperty(authEndpointProperty) != null
            ? prop.getProperty(authEndpointProperty)
            : null;

    if (StringUtils.isEmpty(API_AUTH_ENDPOINT)) {
      Assert.assertTrue(
          StringUtils.isNotEmpty(API_AUTH_ENDPOINT), "API_AUTH_ENDPOINT base malformation");
    }

    return API_AUTH_ENDPOINT;
  }

  public static String getBodyData() {
    return API_BODY_DATA;
  }

  public static String setTestNgEnvironment(String value) {
    API_TESTNG_ENVIRONMENT = value;
    return API_TESTNG_ENVIRONMENT;
  }

  public static String setTestNgClient(String value) {
    API_TESTNG_CLIENT = value;
    return API_TESTNG_CLIENT;
  }

  public static String setTestNgVersion(String value) {
    API_TESTNG_VERSION = value;
    return API_TESTNG_VERSION;
  }
}
