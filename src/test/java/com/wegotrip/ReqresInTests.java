package com.wegotrip;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

public class ReqresInTests {
    @BeforeAll
    static void setUp() {
        //Configuration.baseUrl = "https://reqres.in";
        RestAssured.baseURI = "https://reqres.in";

    }
    @Test
    void listUsersTest() {
        given()
                .log().uri()
                .when()
                .get("/api/users?page=2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("page", is(2))
                .body("data.id", hasItems(7, 8, 9, 10, 11, 12));
    }

    @Test
    void singleUsersTest() {
        String data = "{ \"url\": \"https://reqres.in/#support-heading\", \"text\": " +
                "\"To keep ReqRes free, contributions towards server costs are appreciated!\" }";
        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .get("/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("support.url", is("https://reqres.in/#support-heading"))
                .body("support.text",
                        is("To keep ReqRes free, contributions towards server costs are appreciated!"));
    }

    @Test
    void createTest() {
        String data = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/users")
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void registerSuccessfulTest() {
        String data = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";
        given()
                .log().uri()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", notNullValue());
    }

    @Test
    void updateTest() {
        String data = "{ \"name\": \"morpheus\", \"job\": \"zion resident\" }";
        given()
                .log().uri()
                .body("123")
                .when()
                .put("/api/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200);
    }
}
