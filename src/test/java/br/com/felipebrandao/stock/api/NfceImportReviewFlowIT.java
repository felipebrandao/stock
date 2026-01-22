package br.com.felipebrandao.stock.api;

import br.com.felipebrandao.stock.api.controller.category.dto.request.CreateCategoryRequest;
import br.com.felipebrandao.stock.api.controller.location.dto.request.CreateLocationRequest;
import br.com.felipebrandao.stock.api.controller.nfce.dto.request.UpdateNfceImportReviewItemRequest;
import br.com.felipebrandao.stock.api.controller.nfce.dto.request.UpdateNfceImportReviewRequest;
import br.com.felipebrandao.stock.api.controller.product.dto.request.CreateProductRequest;
import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceStatus;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.repository.NfceImportJpaRepository;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.repository.NfceScrapeJpaRepository;
import br.com.felipebrandao.stock.product.infrastructure.persistence.repository.ProductAliasJpaRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class NfceImportReviewFlowIT {

    @LocalServerPort
    int port;

    @Autowired
    NfceImportJpaRepository nfceImportJpaRepository;

    @Autowired
    NfceScrapeJpaRepository nfceScrapeJpaRepository;

    @Autowired
    ProductAliasJpaRepository productAliasJpaRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Deve bloquear edição da revisão após status APPLIED")
    void shouldBlockReviewUpdateAfterApplied() {
        UUID categoryId = createCategory("Bebidas");
        UUID productId = createProduct("Cerveja", categoryId);
        UUID locationId = createLocation("Despensa");

        String accessKey = "12345678901234567890123456789012345678901234";
        UUID nfceImportId = seedCompletedImportWithScrape(accessKey);

        // força build automático de itens
        given()
                .accept(ContentType.JSON)
                .get("/api/nfce/imports/{id}/review", nfceImportId)
                .then()
                .statusCode(200)
                .body("items", hasSize(1));

        String itemIdStr = given()
                .accept(ContentType.JSON)
                .get("/api/nfce/imports/{id}/review", nfceImportId)
                .then()
                .statusCode(200)
                .extract()
                .path("items[0].id");
        UUID itemId = UUID.fromString(itemIdStr);

        UpdateNfceImportReviewItemRequest item = new UpdateNfceImportReviewItemRequest();
        item.setId(itemId);
        item.setProductId(productId);
        item.setQuantity(new BigDecimal("2.000"));
        item.setLocationId(locationId);
        item.setSaveMapping(false);

        UpdateNfceImportReviewRequest body = new UpdateNfceImportReviewRequest();
        body.setItems(List.of(item));

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .put("/api/nfce/imports/{id}/review", nfceImportId)
                .then()
                .statusCode(204);

        given()
                .post("/api/nfce/imports/{id}/approve", nfceImportId)
                .then()
                .statusCode(204);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .put("/api/nfce/imports/{id}/review", nfceImportId)
                .then()
                .statusCode(400)
                .body("message", containsString("não pode ser editada"));

        var saved = nfceImportJpaRepository.findById(nfceImportId).orElseThrow();
        org.assertj.core.api.Assertions.assertThat(saved.getStatus()).isEqualTo(NfceStatus.APPLIED);
    }

    @Test
    @DisplayName("Deve recusar aprovação quando existe item não mapeado")
    void shouldRejectApproveWhenNotMappedItemsExist() {
        UUID locationId = createLocation("Despensa");

        String accessKey = "99995678901234567890123456789012345678901234";
        UUID nfceImportId = seedCompletedImportWithScrape(accessKey);

        given()
                .accept(ContentType.JSON)
                .get("/api/nfce/imports/{id}/review", nfceImportId)
                .then()
                .statusCode(200);

        String itemIdStr = given()
                .accept(ContentType.JSON)
                .get("/api/nfce/imports/{id}/review", nfceImportId)
                .then()
                .statusCode(200)
                .extract()
                .path("items[0].id");
        UUID itemId = UUID.fromString(itemIdStr);

        UpdateNfceImportReviewItemRequest item = new UpdateNfceImportReviewItemRequest();
        item.setId(itemId);
        item.setProductId(null);
        item.setQuantity(new BigDecimal("1.000"));
        item.setLocationId(locationId);

        UpdateNfceImportReviewRequest body = new UpdateNfceImportReviewRequest();
        body.setItems(List.of(item));

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .put("/api/nfce/imports/{id}/review", nfceImportId)
                .then()
                .statusCode(204);

        given()
                .post("/api/nfce/imports/{id}/approve", nfceImportId)
                .then()
                .statusCode(400)
                .body("message", containsString("itens não mapeados"));
    }

    @Test
    @DisplayName("Deve mapear automaticamente por EAN via product_alias")
    void shouldAutoMapByEan() {
        UUID categoryId = createCategory("Laticínios");
        UUID productId = createProduct("Leite", categoryId);

        // cria alias por EAN
        productAliasJpaRepository.save(
                br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductAliasEntity.builder()
                        .aliasNormalized("leite")
                        .ean("7890000000000")
                        .product(br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductEntity.builder()
                                .id(productId)
                                .build())
                        .createdAt(java.time.OffsetDateTime.now())
                        .build()
        );

        String accessKey = "11115678901234567890123456789012345678901234";
        UUID nfceImportId = seedCompletedImportWithScrape(accessKey);

        given()
                .accept(ContentType.JSON)
                .get("/api/nfce/imports/{id}/review", nfceImportId)
                .then()
                .statusCode(200)
                .body("items[0].ean", equalTo("7890000000000"))
                .body("items[0].productId", equalTo(productId.toString()))
                .body("items[0].status", equalTo("MAPPED"));
    }

    private UUID createCategory(String name) {
        CreateCategoryRequest req = new CreateCategoryRequest();
        req.setName(name);

        String id = given()
                .contentType(ContentType.JSON)
                .body(req)
                .post("/api/categories")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        return UUID.fromString(id);
    }

    private UUID createProduct(String name, UUID categoryId) {
        CreateProductRequest req = new CreateProductRequest();
        req.setName(name);
        req.setCategoryId(categoryId);

        String id = given()
                .contentType(ContentType.JSON)
                .body(req)
                .post("/api/products")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
        return UUID.fromString(id);
    }

    private UUID createLocation(String name) {
        CreateLocationRequest req = new CreateLocationRequest();
        req.setName(name);
        req.setDescription("Default");

        String id = given()
                .contentType(ContentType.JSON)
                .body(req)
                .post("/api/locations")
                .then()
                .statusCode(201)
                .extract()
                .path("id");
        return UUID.fromString(id);
    }

    private UUID seedCompletedImportWithScrape(String accessKey) {
        // nfce_import
        br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceImportEntity imp =
                br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceImportEntity.builder()
                        .accessKey(accessKey)
                        .qrCodeUrl(accessKey)
                        .status(NfceStatus.COMPLETED)
                        .createdAt(java.time.OffsetDateTime.now())
                        .processedAt(java.time.OffsetDateTime.now())
                        .build();
        var savedImport = nfceImportJpaRepository.save(imp);

        // scrape
        br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceScrapeEntity scrape =
                br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceScrapeEntity.builder()
                        .accessKey(accessKey)
                        .uf("SP")
                        .fonte("TEST")
                        .scrapedAt(java.time.OffsetDateTime.now())
                        .build();

        br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceScrapeItemEntity scrapeItem =
                br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceScrapeItemEntity.builder()
                        .numero(1)
                        .descricao("Leite Italac")
                        .quantidade(new BigDecimal("1.000"))
                        .unidade("UN")
                        .valor(new BigDecimal("5.00"))
                        .valorUnitario(new BigDecimal("5.00"))
                        .ean("7890000000000")
                        .build();

        scrapeItem.setScrape(scrape);
        scrape.setItens(List.of(scrapeItem));

        nfceScrapeJpaRepository.save(scrape);

        return savedImport.getId();
    }
}
