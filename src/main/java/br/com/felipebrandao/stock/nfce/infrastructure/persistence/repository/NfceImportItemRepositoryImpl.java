package br.com.felipebrandao.stock.nfce.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImportItem;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportItemRepository;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.mapper.NfceImportItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NfceImportItemRepositoryImpl implements NfceImportItemRepository {

    private final NfceImportItemJpaRepository jpaRepository;
    private final NfceImportItemMapper mapper;

    @Override
    public List<NfceImportItem> findByNfceImportId(UUID nfceImportId) {
        return jpaRepository.findByNfceImport_Id(nfceImportId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<NfceImportItem> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public NfceImportItem save(NfceImportItem item) {
        var saved = jpaRepository.save(mapper.toEntity(item));
        return mapper.toDomain(saved);
    }

    @Override
    public List<NfceImportItem> saveAll(List<NfceImportItem> items) {
        var entities = items.stream().map(mapper::toEntity).toList();
        return jpaRepository.saveAll(entities).stream().map(mapper::toDomain).toList();
    }
}
