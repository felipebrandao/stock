package br.com.felipebrandao.stock.nfce.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceImportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NfceImportJpaRepository extends JpaRepository<NfceImportEntity, UUID> {

    Optional<NfceImportEntity> findByAccessKey(String accessKey);

    @Query(value = "SELECT n " +
            "FROM NfceImportEntity n " +
            "WHERE n.status = 'PENDING' " +
            "ORDER BY n.createdAt ASC")
    List<NfceImportEntity> findNextPending(int limit);
}

