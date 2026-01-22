package br.com.felipebrandao.stock.nfce.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceImportItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NfceImportItemJpaRepository extends JpaRepository<NfceImportItemEntity, UUID> {

    List<NfceImportItemEntity> findByNfceImport_Id(UUID nfceImportId);
}
