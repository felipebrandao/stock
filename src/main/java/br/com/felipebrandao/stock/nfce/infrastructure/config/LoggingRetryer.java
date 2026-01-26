package br.com.felipebrandao.stock.nfce.infrastructure.config;

import feign.RetryableException;
import feign.Retryer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingRetryer implements Retryer {

    private final long period;
    private final long maxPeriod;
    private final int maxAttempts;
    private int attempt;

    public LoggingRetryer(long period, long maxPeriod, int maxAttempts) {
        this.period = period;
        this.maxPeriod = maxPeriod;
        this.maxAttempts = maxAttempts;
        this.attempt = 1;
    }

    @Override
    public void continueOrPropagate(RetryableException e) {
        if (attempt++ >= maxAttempts) {
            log.error("[feign-retry] Máximo de tentativas atingido ({}/{}). Propagando erro: {}",
                    attempt - 1, maxAttempts, e.getMessage());
            throw e;
        }

        long interval = nextMaxInterval();
        log.warn("[feign-retry] Tentativa {}/{} falhou. Aguardando {} ms antes de tentar novamente. Erro: {}",
                attempt - 1, maxAttempts, interval, e.getMessage());

        try {
            Thread.sleep(interval);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
            throw e;
        }
    }

    long nextMaxInterval() {
        long interval = (long) (period * Math.pow(1.5, (double) attempt - 1));
        return Math.min(interval, maxPeriod);
    }

    @Override
    public Retryer clone() {
        return new LoggingRetryer(period, maxPeriod, maxAttempts);
    }
}
