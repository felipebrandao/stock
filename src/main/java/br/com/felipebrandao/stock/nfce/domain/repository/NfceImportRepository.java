package br.com.felipebrandao.stock.nfce.domain.repository;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NfceImportRepository {

    NfceImport save(NfceImport importJob);
    List<NfceImport> findNextPending(int limit);
    Optional<NfceImport> findById(UUID id);
}


