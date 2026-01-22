package br.com.felipebrandao.stock.api;

import br.com.felipebrandao.stock.api.controller.category.dto.request.CreateCategoryRequest;
import br.com.felipebrandao.stock.api.controller.location.dto.request.CreateLocationRequest;
import br.com.felipebrandao.stock.api.controller.product.dto.request.CreateProductRequest;
import br.com.felipebrandao.stock.api.controller.stock.dto.request.ConvertStockItemRequest;
import br.com.felipebrandao.stock.api.controller.stock.dto.request.CreateStockMovementRequest;
import br.com.felipebrandao.stock.api.controller.stock.dto.request.OpenStockItemRequest;
import br.com.felipebrandao.stock.api.controller.stock.dto.request.TransferStockItemRequest;
import br.com.felipebrandao.stock.stock.domain.model.StockItemState;
import br.com.felipebrandao.stock.stock.domain.model.StockMovementType;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StockItemFlowsIT {

    @LocalServerPort
    int port;

    @Test
    void shouldOpenTransferAndConvert() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;

        UUID categoryId = createCategory("Meat");
        UUID productId = createProduct("Ground beef", categoryId);

        UUID pantryId = createLocation("Pantry");
        UUID fridgeId = createLocation("Fridge");

        // IN creates a CLOSED lot in pantry
        CreateStockMovementRequest in = new CreateStockMovementRequest();
        in.setType(StockMovementType.IN);
        in.setProductId(productId);
        in.setQuantity(new BigDecimal("2"));
        in.setToLocationId(pantryId);

        RestAssured.given().contentType(ContentType.JSON).body(in)
                .when().post("/api/stock-movements")
                .then().statusCode(200);

        String closedLotId = RestAssured.given()
                .queryParam("productId", productId)
                .queryParam("locationId", pantryId)
                .queryParam("state", StockItemState.CLOSED.name())
                .when().get("/api/stock-items")
                .then().statusCode(200)
                .body("content", not(empty()))
                .extract().path("content[0].id");

        // OPEN 1 unit -> creates a new IN_USE lot in fridge, reduces CLOSED lot
        OpenStockItemRequest open = new OpenStockItemRequest();
        open.setQuantity(new BigDecimal("1"));
        open.setTargetLocationId(fridgeId);

        String inUseLotId = RestAssured.given().contentType(ContentType.JSON).body(open)
                .when().post("/api/stock-items/" + closedLotId + "/open")
                .then().statusCode(200)
                .body("state", equalTo(StockItemState.IN_USE.name()))
                .body("locationId", equalTo(fridgeId.toString()))
                .extract().path("id");

        RestAssured.given().when().get("/api/stock-items/" + closedLotId)
                .then().statusCode(200)
                .body("quantity", equalTo(1))
                .body("state", equalTo(StockItemState.CLOSED.name()));

        // TRANSFER the IN_USE lot back to pantry
        TransferStockItemRequest transfer = new TransferStockItemRequest();
        transfer.setTargetLocationId(pantryId);

        RestAssured.given().contentType(ContentType.JSON).body(transfer)
                .when().post("/api/stock-items/" + inUseLotId + "/transfer")
                .then().statusCode(204);

        RestAssured.given().when().get("/api/stock-items/" + inUseLotId)
                .then().statusCode(200)
                .body("locationId", equalTo(pantryId.toString()))
                .body("state", equalTo(StockItemState.IN_USE.name()));

        // CONVERT the IN_USE lot into two lots (0.4 + 0.6)
        ConvertStockItemRequest convert = new ConvertStockItemRequest();
        convert.setDestinationQuantities(List.of(new BigDecimal("0.4"), new BigDecimal("0.6")));

        List<String> createdIds = RestAssured.given().contentType(ContentType.JSON).body(convert)
                .when().post("/api/stock-items/" + inUseLotId + "/convert")
                .then().statusCode(200)
                .extract().as(List.class);

        // source lot should now be CONVERTED (quantity becomes 0)
        RestAssured.given().when().get("/api/stock-items/" + inUseLotId)
                .then().statusCode(200)
                .body("quantity", equalTo(0))
                .body("state", anyOf(equalTo(StockItemState.CONVERTED.name()), equalTo(StockItemState.IN_USE.name())));

        // created lots exist
        RestAssured.given().when().get("/api/stock-items/" + createdIds.get(0))
                .then().statusCode(200)
                .body("state", equalTo(StockItemState.IN_USE.name()));
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
