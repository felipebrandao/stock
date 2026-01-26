package br.com.felipebrandao.stock.nfce.infrastructure.config;

import feign.Request;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {

    @Value("${feign.connect.timeout:10000}")
    private int connectTimeout;

    @Value("${feign.read.timeout:900000}") // 15 minutos
    private int readTimeout;

    @Bean
    public Retryer retryer() {
        // período inicial: 5 segundos, período máximo: 30 segundos, 3 tentativas
        return new LoggingRetryer(5000L, 30000L, 3);
    }

    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(connectTimeout, TimeUnit.MILLISECONDS, readTimeout, TimeUnit.MILLISECONDS, true);
    }
}
