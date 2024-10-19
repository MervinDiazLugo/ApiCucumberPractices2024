package config.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.SkipException;

import java.net.URI;
import java.net.URISyntaxException;


@Log
public class RestAssuredExtension extends RestAssuredConfigProperties{

    String apiVersion = "";
    String apiUri  = "";
    RequestSpecBuilder apiBuilder = new RequestSpecBuilder();


    public RestAssuredExtension() {
        apiVersion = getApiVersion();
        apiUri = StringUtils.isNotEmpty(apiVersion)
                ? getBaseUri().concat(apiVersion)
                : getBaseUri();
        try {
            apiBuilder.setBaseUri(apiUri);
            apiBuilder.setContentType(ContentType.JSON);
            apiBuilder.setAccept("*/*");
        } catch (IllegalArgumentException e) {
            log.info("Base URI cannot be null, check configProperties");
        }
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

    public ResponseOptions<Response> apiDelete(String path) {
        path = insertParams(path);
        try {
            RequestSpecification requestToken = RestAssured.given().spec(apiBuilder.build());
            response = requestToken.delete(path);
            responseBody = response.getBody();
            jsonPathResponse = response.body().jsonPath();

        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SkipException("Api delete failed " + e);
        }
        return response;
    }

    public void assertStatusCode(int code){
        Assert.assertTrue(response.statusCode() == code,
                String.format("Status code in response is %s but expected is %s",
                        String.valueOf(response.statusCode()), code));
    }

    public void assertKeyMessages(String key, String value){
        String val = insertParams(value);
        String responseValue = retrieveJsonPathResponse(key);
        Assert.assertTrue(
                StringUtils.equals(responseValue, val),
                String.format(
                        "Values does not match, response is %s and expected is %s", val, responseValue));
    }


}
