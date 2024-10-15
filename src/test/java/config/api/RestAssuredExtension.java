package config.api;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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

    public Response response = null;

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


    public ResponseOptions<Response> apiGet(String path) {
        path = insertParams(path);
        try {
            RequestSpecification requestToken = RestAssured.given().spec(apiBuilder.build());
            response = requestToken.get(path);

        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SkipException("Api Get failed " + e);
        }
        return response;
    }

    public void assertStatusCode(int code){
        Assert.assertTrue(response.statusCode() == code,
                String.format("Status code in response is %s but expected is %s",
                        String.valueOf(response.statusCode()), code));
    }
}
