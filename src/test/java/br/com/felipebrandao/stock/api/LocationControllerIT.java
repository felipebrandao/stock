package br.com.felipebrandao.stock.api;

import br.com.felipebrandao.stock.api.controller.location.dto.request.CreateLocationRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LocationControllerIT {

    @LocalServerPort
    int port;

    @Test
    void shouldCreateAndListLocations() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        CreateLocationRequest request = new CreateLocationRequest();
        request.setName("Pantry");
        request.setDescription("Main pantry");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/locations")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Pantry"));

        RestAssured.given()
                .when()
                .get("/api/locations")
                .then()
                .statusCode(200)
                .body("content", not(empty()))
                .body("content[0].id", notNullValue());
    }
}
