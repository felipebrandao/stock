package br.com.felipebrandao.stock.category.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.category.domain.model.Category;
import br.com.felipebrandao.stock.category.domain.repository.CategoryRepository;
import br.com.felipebrandao.stock.category.infrastructure.persistence.entity.CategoryEntity;
import br.com.felipebrandao.stock.category.infrastructure.persistence.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;
    private final CategoryMapper mapper;

    @Override
    public Category save(Category category) {
        CategoryEntity entity = mapper.toEntity(category);
        jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Category> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Category> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }
}
