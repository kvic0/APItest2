package TestN2;
import POJO.LombokUserData;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BookStoreTests {


    @DataProvider(name = "isbnData")
    public Object[][] createIsbnData() {
        return new Object[][] {
                { "9781449331818" },
                { "9781449337711" },
                { "9781449365035" },
                { "9781491904244" }
        };
    }

    @Test(dataProvider = "isbnData")
    public void getBookByIsbnTest(String isbn) {

        RestAssured.baseURI = "https://bookstore.toolsqa.com";
        Response response = RestAssured
                .given()
                .queryParam("ISBN", isbn)
                .when()
                .get("/BookStore/v1/Book")
                .then()
                .log().all()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("isbn"), isbn);
    }

    @Test
    public void createUserWithInvalidPassword() {

        RestAssured.baseURI = "https://bookstore.toolsqa.com";


        LombokUserData.UserRequest invalidData = LombokUserData.UserRequest.builder()
                .userName("invalidUser_" + System.currentTimeMillis())
                .password("123")
                .build();


        String expectedMessage = "Passwords must have at least one non alphanumeric character, " +
                "one digit ('0'-'9'), one uppercase ('A'-'Z'), one lowercase ('a'-'z'), " +
                "one special character and Password must be eight characters or longer.";

        Response response = RestAssured
                .given()
                .contentType(io.restassured.http.ContentType.JSON)
                .body(invalidData)
                .when()
                .post("/Account/v1/User")
                .then()
                .log().all()
                .extract().response();


        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertEquals(response.jsonPath().getString("message"), expectedMessage);
    }
}