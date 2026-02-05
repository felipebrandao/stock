package br.com.felipebrandao.stock.nfce.domain.repository;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NfceImportRepository {

    NfceImport save(NfceImport importJob);
    Optional<NfceImport> findById(UUID id);

    List<NfceImport> findAllPaginated(int page, int size);
    List<NfceImport> findByStatusPaginated(NfceStatus status, int page, int size);
    long count();
    long countByStatus(NfceStatus status);
}


