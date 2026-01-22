package br.com.felipebrandao.stock.nfce.domain.repository;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImportItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NfceImportItemRepository {

    List<NfceImportItem> findByNfceImportId(UUID nfceImportId);

    Optional<NfceImportItem> findById(UUID id);

    NfceImportItem save(NfceImportItem item);

    List<NfceImportItem> saveAll(List<NfceImportItem> items);
}
