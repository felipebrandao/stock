package br.com.felipebrandao.stock.api;

import br.com.felipebrandao.stock.api.controller.category.dto.request.CreateCategoryRequest;
import br.com.felipebrandao.stock.api.controller.location.dto.request.CreateLocationRequest;
import br.com.felipebrandao.stock.api.controller.product.dto.request.CreateProductRequest;
import br.com.felipebrandao.stock.api.controller.stock.dto.request.ConsumeStockItemRequest;
import br.com.felipebrandao.stock.api.controller.stock.dto.request.CreateStockMovementRequest;
import br.com.felipebrandao.stock.stock.domain.model.StockItemState;
import br.com.felipebrandao.stock.stock.domain.model.StockMovementType;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.UUID;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StockMovementControllerIT {

    @LocalServerPort
    int port;

    @Test
    void shouldCreateInMovementAndThenConsumeByItemId() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        UUID categoryId = createCategory("Dairy");
        UUID productId = createProduct("Milk", categoryId);
        UUID locationId = createLocation("Pantry");

        CreateStockMovementRequest move = new CreateStockMovementRequest();
        move.setType(StockMovementType.IN);
        move.setProductId(productId);
        move.setQuantity(new java.math.BigDecimal("5"));
        move.setToLocationId(locationId);
        move.setNote("manual entry");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(move)
                .when()
                .post("/api/stock-movements")
                .then()
                .statusCode(200)
                .body("id", notNullValue());

        // now, stock-items returns lots; take the first one and ensure it's CLOSED
        String stockItemId = RestAssured.given()
                .queryParam("productId", productId)
                .queryParam("locationId", locationId)
                .when()
                .get("/api/stock-items")
                .then()
                .statusCode(200)
                .body("content", not(empty()))
                .body("content[0].quantity", equalTo(5))
                .body("content[0].state", equalTo(StockItemState.CLOSED.name()))
                .extract().path("content[0].id");

        ConsumeStockItemRequest consume = new ConsumeStockItemRequest();
        consume.setQuantity(new java.math.BigDecimal("5"));
        consume.setNote("use all");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(consume)
                .when()
                .post("/api/stock-items/" + stockItemId + "/consume")
                .then()
                .statusCode(204);

        RestAssured.given()
                .when()
                .get("/api/stock-items/" + stockItemId)
                .then()
                .statusCode(200)
                .body("quantity", equalTo(0))
                .body("state", equalTo(StockItemState.WRITTEN_OFF.name()));
    }

    private UUID createCategory(String name) {
        CreateCategoryRequest req = new CreateCategoryRequest();
        req.setName(name);

        String id = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/api/categories")
                .then()
                .statusCode(200)
                .extract().path("id");

        return UUID.fromString(id);
    }

    private UUID createProduct(String name, UUID categoryId) {
        CreateProductRequest req = new CreateProductRequest();
        req.setName(name);
        req.setCategoryId(categoryId);

        String id = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/api/products")
                .then()
                .statusCode(200)
                .extract().path("id");

        return UUID.fromString(id);
    }

    private UUID createLocation(String name) {
        CreateLocationRequest req = new CreateLocationRequest();
        req.setName(name);

        String id = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/api/locations")
                .then()
                .statusCode(200)
                .extract().path("id");

        return UUID.fromString(id);
    }
}
