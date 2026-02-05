package br.com.felipebrandao.stock.nfce.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceStatus;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceImportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NfceImportJpaRepository extends JpaRepository<NfceImportEntity, UUID> {

    Optional<NfceImportEntity> findByAccessKey(String accessKey);


    Page<NfceImportEntity> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<NfceImportEntity> findByStatusOrderByCreatedAtDesc(NfceStatus status, Pageable pageable);

    long countByStatus(NfceStatus status);
}

