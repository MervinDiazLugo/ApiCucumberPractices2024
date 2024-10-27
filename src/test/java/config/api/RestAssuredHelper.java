package config.api;

import io.cucumber.datatable.DataTable;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.testng.SkipException;

@Log
public class RestAssuredHelper {
  public static JSONObject testData = new JSONObject();

  public JsonPath jsonPathResponse;
  public ResponseBody responseBody;
  public Response response;

  public RestAssuredHelper() {
    testData.put("username", "admin");
    testData.put("password", "password123");

    testData.put("firstname", "Tom Sawyer");
    testData.put("totalprice", 60000);
    testData.put("depositpaid", true);
  }

  public String getFileBody(String file) {
    String bodyPath;
    try {
      bodyPath =
          new String(
              Files.readAllBytes(Paths.get(getCurrentPath() + "/src/test/resources/" + file)));
    } catch (Exception e) {
      throw new SkipException("check configProperties or path variable " + e.getMessage());
    }

    return bodyPath;
  }

  public String getCurrentPath() {
    return Paths.get("").toAbsolutePath().toString();
  }

  public String insertParams(String stringData) {
    StringBuffer stringbuffer = new StringBuffer();
    Pattern pattern = Pattern.compile("\\$(\\w+)");
    Matcher matcher;
    String replacement = null;
    if (StringUtils.isEmpty(stringData)) {
      throw new SkipException("String is empty");
    }
    if (testData.isEmpty()) {
      throw new SkipException("Test Data is empty");
    }
    matcher = pattern.matcher(stringData);
    while (matcher.find()) {
      String varName = matcher.group(1);
      boolean keyExist = testData.containsKey(varName);
      replacement = keyExist ? testData.get(varName).toString() : stringData;

      if (StringUtils.isNotEmpty(replacement)) {
        matcher.appendReplacement(
            stringbuffer,
            matcher.group(1).replaceFirst(Pattern.quote(matcher.group(1)), replacement));
      }
    }
    if (StringUtils.isNotEmpty(stringbuffer)) {
      matcher.appendTail(stringbuffer);
      stringData = stringbuffer.toString();
    }
    return stringData;
  }

  public String retrieveResponse(String key) {
    String value = "";
    ArrayList<String> arrayValue = new ArrayList<>();
    String classType = response.getBody().path(key).toString();
    if (response != null && response.getBody() != null) {
      if (response.getBody().path(key) != null) {
        if ("class java.util.ArrayList".equals(classType)) {
          arrayValue = response.getBody().path(key);
          value = StringUtils.equals(arrayValue.toString(), "[]") ? "" : arrayValue.toString();
        } else {
          value = response.getBody().path(key).toString();
        }
      } else {
        throw new SkipException("Selected path didn't exist ");
      }
    } else {
      throw new SkipException("Response is null or empty");
    }

    return value;
  }

  public String retrieveJsonPathResponse(String key) {
    String value = "";
    if (jsonPathResponse != null && StringUtils.isNotEmpty(jsonPathResponse.get(key).toString())) {
      value = jsonPathResponse.get(key).toString();
    } else {
      throw new SkipException("Response is null or empty");
    }
    return value;
  }

  public void saveInTestData(String key, String value) {
    if (StringUtils.isNotEmpty(key)) {
      if (testData.containsKey(key)) {
        testData.replace(key, value);
      } else {
        testData.put(key, value);
      }
      log.info("Test Data updated: " + testData.toString());
    } else {
      log.info("testData data: value was empty");
    }
  }

  /**
   * Create a table with parameters given on feature step.
   *
   * @param table is a list with parameters given on step.
   */
  public DataTable createDataTable(List<List<String>> table) {
    DataTable data;
    data = DataTable.create(table);
    log.info(data.toString());
    return data;
  }

  public void getDataFromTable(List<List<String>> table) {
    DataTable data = createDataTable(table);
    if (data != null) {
      data.cells()
          .forEach(
              value -> {
                // create variables as columns you have.
                String key = value.get(0);
                String val = value.get(1);
                if (StringUtils.isNotEmpty(key)) {
                  saveInTestData(key, val);
                }
              });
    }
  }

  public JSONObject setCredentials() {
    JSONObject credentials = new JSONObject();
    credentials.put("username", "admin");
    credentials.put("password", "password123");

    return credentials;
  }
}
