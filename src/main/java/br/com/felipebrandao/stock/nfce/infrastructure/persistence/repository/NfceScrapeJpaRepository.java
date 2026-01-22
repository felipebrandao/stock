package br.com.felipebrandao.stock.nfce.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceScrapeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface NfceScrapeJpaRepository extends JpaRepository<NfceScrapeEntity, UUID> {
    Optional<NfceScrapeEntity> findByAccessKey(String accessKey);
    boolean existsByAccessKey(String accessKey);
}
