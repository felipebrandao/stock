package br.com.felipebrandao.stock.nfce;

import br.com.felipebrandao.stock.nfce.application.port.out.NfceScraperClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class NfceScraperClientWireMockIT {

    static WireMockServer wireMockServer;

    @Autowired
    NfceScraperClient client;

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("nfce.scraper.url", () -> "http://localhost:9561");
    }

    @BeforeEach
    void start() {
        wireMockServer = new WireMockServer(9561);
        wireMockServer.start();
        configureFor("localhost", 9561);
    }

    @AfterEach
    void stop() {
        wireMockServer.stop();
    }

    @Test
    @DisplayName("Deve chamar o scraper de NFC-e e receber resposta mapeada")
    void shouldCallScraper() {
        stubFor(post(urlEqualTo("/scrape"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"accessKey\":\"123\",\"uf\":\"SP\",\"fonte\":\"x\",\"scrapedAt\":\"2026-01-01T00:00:00Z\",\"identificacao\":{\"modelo\":65,\"serie\":1,\"numero\":1},\"emitente\":{\"razaoSocial\":\"Teste\"},\"itens\":[]}")));

        var resp = client.scrape("url");
        assertNotNull(resp);
        assertEquals("SP", resp.getUf());
        assertNotNull(resp.getScrapedAt());
    }
}
