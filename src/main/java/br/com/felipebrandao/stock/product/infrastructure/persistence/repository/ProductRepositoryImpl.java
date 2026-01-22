package br.com.felipebrandao.stock.product.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.product.domain.model.Product;
import br.com.felipebrandao.stock.product.domain.repository.ProductRepository;
import br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductEntity;
import br.com.felipebrandao.stock.product.infrastructure.persistence.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository jpaRepository;
    private final ProductMapper mapper;

    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByNameAndCategoryId(String name, UUID categoryId) {
        return jpaRepository.existsByNameIgnoreCaseAndCategory_Id(name, categoryId);
    }
}
