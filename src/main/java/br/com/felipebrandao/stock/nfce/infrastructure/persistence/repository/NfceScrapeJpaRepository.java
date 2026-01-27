package br.com.felipebrandao.stock.nfce.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceScrapeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface NfceScrapeJpaRepository extends JpaRepository<NfceScrapeEntity, UUID> {
    Optional<NfceScrapeEntity> findByAccessKey(String accessKey);

    @Query("SELECT s FROM NfceScrapeEntity s LEFT JOIN FETCH s.itens WHERE s.accessKey = :accessKey")
    Optional<NfceScrapeEntity> findByAccessKeyWithItems(@Param("accessKey") String accessKey);

    boolean existsByAccessKey(String accessKey);
}
