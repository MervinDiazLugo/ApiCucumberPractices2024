package config.api;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import com.github.fge.jsonschema.SchemaVersion;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.SkipException;

@Log
public class RestAssuredExtension extends RestAssuredConfigProperties {

  String apiVersion = "";
  String apiUri = "";
  RequestSpecBuilder apiBuilder = new RequestSpecBuilder();
  public String authEndpoint;
  Response authToken;

  public boolean isAlreadyAuthenticated;

  public RestAssuredExtension() {
    apiVersion = getApiVersion();
    apiUri = StringUtils.isNotEmpty(apiVersion) ? getBaseUri().concat(apiVersion) : getBaseUri();
    try {
      authentication();
      apiBuilder.setBaseUri(apiUri);
      apiBuilder.setContentType(ContentType.JSON);
      apiBuilder.setAccept("application/json");
    } catch (IllegalArgumentException e) {
      log.info("Base URI cannot be null, check configProperties");
    }
  }

  /** get response from GraphQL Api */
  public void authentication() {
    RequestSpecBuilder authBuilder = new RequestSpecBuilder();
    authBuilder.setBaseUri(apiUri);
    authEndpoint = getAuthenticationEndpoint();

    if (StringUtils.contains(apiUri, "petstore.swagger.io")) {
      apiBuilder.addHeader("api_key", "special-key");
      isAlreadyAuthenticated = true;
    } else if (StringUtils.contains(apiUri, "restful-booker.herokuapp.com")) {
      isAlreadyAuthenticated = setBearerToken();
      if (!isAlreadyAuthenticated) {
        bookerAuth(authBuilder);
      }
    }
  }

  private void bookerAuth(RequestSpecBuilder authBuilder) {
    try {
      authBuilder.addHeader("Content-Type", "application/json");
      authBuilder.addHeader("Accept", "application/json");
      String body = getFileBody("BodyBooker/credentials.json");
      body = insertParams(body);
      authBuilder.setBody(body);
      RequestSpecification requestToken = RestAssured.given().spec(authBuilder.build());
      authToken = requestToken.post(authEndpoint);
      if (authToken.getStatusCode() != 200) {
        throw new SkipException("Authentication failed " + authToken.getStatusCode());
      } else {
        String token =
            StringUtils.isNotEmpty(authToken.getBody().jsonPath().get("token"))
                ? authToken.getBody().jsonPath().get("token")
                : null;
        if (StringUtils.isNotEmpty(token)) {
          saveInTestData("token", token);
          setBearerToken();
        }
      }
    } catch (IllegalArgumentException | NullPointerException e) {
      throw new SkipException("Booker Authentication failed " + e.getMessage());
    }
  }

  public boolean setBearerToken() {
    String token = testData.containsKey("token") ? testData.get("token").toString() : null;
    boolean isAlreadyAuthenticated = StringUtils.isNotEmpty(token);
    if (isAlreadyAuthenticated) {
      apiBuilder.addHeader("Cookie", String.format("token=%s", token));
    }
    return isAlreadyAuthenticated;
  }

  public ResponseOptions<Response> apiGet(String endpoint) {
    endpoint = insertParams(endpoint);
    try {
      RequestSpecification requestToken = RestAssured.given().spec(apiBuilder.build());
      response = requestToken.get(endpoint);
      responseBody = response.getBody();
      jsonPathResponse = response.body().jsonPath();

    } catch (IllegalArgumentException | NullPointerException e) {
      throw new SkipException("Api Get failed " + e);
    }
    return response;
  }

  public ResponseOptions<Response> apiPost(String endpoint, String bodyPath) {
    endpoint = insertParams(endpoint);
    try {
      String body = getFileBody(bodyPath);

      if (StringUtils.isNotEmpty(body)) {
        body = insertParams(body);
      } else {
        throw new SkipException("Body is Empty");
      }
      apiBuilder.setBody(body);
      log.info(body);
      RequestSpecification requestToken = RestAssured.given().spec(apiBuilder.build());
      response = requestToken.post(endpoint);
      responseBody = response.getBody();
      jsonPathResponse = response.body().jsonPath();

    } catch (IllegalArgumentException | NullPointerException e) {
      throw new SkipException("Authentication failed " + e);
    }
    return response;
  }

  public ResponseOptions<Response> apiPut(String endpoint, String bodyPath) {
    endpoint = insertParams(endpoint);
    try {
      String body = getFileBody(bodyPath);
      if (StringUtils.isNotEmpty(body)) {
        body = insertParams(body);
      } else {
        throw new SkipException("Body is Empty");
      }
      apiBuilder.setBody(body);
      log.info(body);
      RequestSpecification requestToken = RestAssured.given().spec(apiBuilder.build());
      response = requestToken.put(endpoint);
      responseBody = response.getBody();
      jsonPathResponse = response.body().jsonPath();

    } catch (IllegalArgumentException | NullPointerException e) {
      throw new SkipException("Authentication failed " + e);
    }
    return response;
  }

  public ResponseOptions<Response> apiDelete(String endpoint) {
    endpoint = insertParams(endpoint);
    try {
      RequestSpecification requestToken = RestAssured.given().spec(apiBuilder.build());
      response = requestToken.delete(endpoint);
      responseBody = response.getBody();
      jsonPathResponse = response.body().jsonPath();

    } catch (IllegalArgumentException | NullPointerException e) {
      throw new SkipException("Api delete failed " + e);
    }
    return response;
  }

  public void assertStatusCode(int code) {
    Assert.assertTrue(
        response.statusCode() == code,
        String.format(
            "Status code in response is %s but expected is %s",
            String.valueOf(response.statusCode()), code));
  }

  public void assertKeyMessages(String key, String value) {
    String val = insertParams(value);
    String responseValue = retrieveJsonPathResponse(key);

    if (StringUtils.equals(val, "NOT NULL")) {
      Assert.assertTrue(
          StringUtils.isNotEmpty(responseValue),
          String.format("NOT NULL: response is %s and expected is %s", val, responseValue));
    } else if (StringUtils.equals(val, "IS A NUMBER")) {
      Assert.assertTrue(
          StringUtils.isNumeric(responseValue),
          String.format("IS A NUMBER: response is %s and expected is %s", val, responseValue));
    } else {
      Assert.assertTrue(
          StringUtils.equals(responseValue, val),
          String.format(
              "Values does not match, response is %s and expected is %s", val, responseValue));
    }
  }

  public void assertResponseFromTable(List<List<String>> table) {
    DataTable data = createDataTable(table);
    if (data != null) {
      data.cells()
          .forEach(
              value -> {
                // create variables as columns you have.
                String key = value.get(0);
                String val = value.get(1);
                if (StringUtils.isNotEmpty(key)) {
                  assertKeyMessages(key, val);
                }
              });
    }
  }

  public void assertJsonSchemaValidation(String schemaFile) {
    response
        .then()
        .assertThat()
        .body(matchesJsonSchema(getFileBody(schemaFile)).using(jsonSchemaFactory()));
  }

  public JsonSchemaFactory jsonSchemaFactory() {
    JsonSchemaFactory jsonSchemaFactory =
        JsonSchemaFactory.newBuilder()
            .setValidationConfiguration(
                ValidationConfiguration.newBuilder()
                    .setDefaultVersion(SchemaVersion.DRAFTV4)
                    .freeze())
            .freeze();

    return jsonSchemaFactory;
  }
}
