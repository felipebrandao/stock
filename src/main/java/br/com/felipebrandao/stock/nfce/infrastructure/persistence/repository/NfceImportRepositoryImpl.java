package br.com.felipebrandao.stock.nfce.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceStatus;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceImportEntity;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.mapper.NfceImportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NfceImportRepositoryImpl implements NfceImportRepository {

    private final NfceImportJpaRepository jpaRepository;
    private final NfceImportMapper mapper;

    @Override
    public NfceImport save(NfceImport importJob) {
        NfceImportEntity entity = mapper.toEntity(importJob);
        NfceImportEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }


    @Override
    public Optional<NfceImport> findById(UUID id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public List<NfceImport> findAllPaginated(int page, int size) {
        return jpaRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size))
                .map(mapper::toDomain)
                .getContent();
    }

    @Override
    public List<NfceImport> findByStatusPaginated(NfceStatus status, int page, int size) {
        return jpaRepository.findByStatusOrderByCreatedAtDesc(status, PageRequest.of(page, size))
                .map(mapper::toDomain)
                .getContent();
    }

    @Override
    public long count() {
        return jpaRepository.count();
    }

    @Override
    public long countByStatus(NfceStatus status) {
        return jpaRepository.countByStatus(status);
    }

}
