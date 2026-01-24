package br.com.felipebrandao.stock.api.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties(CorsProperties.class)
public class CorsConfig implements WebMvcConfigurer {

    private final CorsProperties corsProperties;

    public CorsConfig(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        if (corsProperties.getAllowedOrigins() == null || corsProperties.getAllowedOrigins().isEmpty()) {
            return;
        }

        var mapping = registry.addMapping("/**")
                .allowedOrigins(corsProperties.getAllowedOrigins().stream().filter(StringUtils::hasText).toArray(String[]::new))
                .allowedMethods(corsProperties.getAllowedMethods().toArray(String[]::new))
                .allowedHeaders(corsProperties.getAllowedHeaders().toArray(String[]::new))
                .allowCredentials(corsProperties.isAllowCredentials())
                .maxAge(corsProperties.getMaxAge());

        if (corsProperties.getExposedHeaders() != null && !corsProperties.getExposedHeaders().isEmpty()) {
            mapping.exposedHeaders(corsProperties.getExposedHeaders().toArray(String[]::new));
        }
    }
}
