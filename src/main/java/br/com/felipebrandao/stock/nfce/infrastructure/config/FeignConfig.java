package br.com.felipebrandao.stock.nfce.infrastructure.config;

import feign.Request;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {

    @Value("${feign.connect.timeout:5000}")
    private int connectTimeout;

    @Value("${feign.read.timeout:900000}")
    private int readTimeout;

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000L, TimeUnit.SECONDS.toMillis(10), 3);
    }

    @Bean
    public Request.Options feignOptions() {
        return new Request.Options(connectTimeout, readTimeout);
    }
}
