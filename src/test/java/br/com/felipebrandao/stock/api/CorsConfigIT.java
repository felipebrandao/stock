package br.com.felipebrandao.stock.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        // CORS
        "app.cors.allowed-origins=http://localhost:5173",
        "app.cors.allowed-methods=GET,POST,PUT,PATCH,DELETE,OPTIONS",
        "app.cors.allowed-headers=Authorization,Content-Type,Accept,Origin",
        "app.cors.allow-credentials=false",
        "app.cors.max-age=3600",

        // Test infra (não depender de Postgres/Flyway)
        "spring.flyway.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:cors_it;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.jpa.open-in-view=false"
})
class CorsConfigIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldAnswerPreflightWhenOriginIsAllowed() throws Exception {
        mockMvc.perform(options("/api-docs")
                        .header(HttpHeaders.ORIGIN, "http://localhost:5173")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "Content-Type"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:5173"));
    }

    @Test
    void shouldIncludeCorsHeadersInSimpleRequestWhenOriginIsAllowed() throws Exception {
        mockMvc.perform(get("/api-docs")
                        .header(HttpHeaders.ORIGIN, "http://localhost:5173"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "http://localhost:5173"));
    }
}
