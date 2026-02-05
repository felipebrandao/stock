package br.com.felipebrandao.stock.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "nfceImportExecutor")
    public Executor nfceImportExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("nfce-import-");
        executor.setRejectedExecutionHandler((r, exec) -> {
            log.error("[async] Tarefa rejeitada pelo executor. Fila cheia.");
        });
        executor.initialize();
        return executor;
    }
}
