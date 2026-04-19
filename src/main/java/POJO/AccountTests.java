package POJO;

import POJO.LombokUserData;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AccountTests {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://bookstore.toolsqa.com";
    }

    @Test
    public void createNewUserTest() {

        LombokUserData.UserRequest userData = LombokUserData.UserRequest.builder()
                .userName("testUser_" + System.currentTimeMillis())
                .password("TestPassword123!")
                .build();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(userData)
                .when()
                .post("/Account/v1/User")
                .then()
                .log().all()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 201, "მომხმარებელი ვერ შეიქმნა!");

        String responseUsername = response.jsonPath().getString("username");
        String userID = response.jsonPath().getString("userID");

        Assert.assertEquals(responseUsername, userData.getUserName(), "Username არ ემთხვევა!");
        Assert.assertNotNull(userID, "userID ცარიელია!");
    }
}