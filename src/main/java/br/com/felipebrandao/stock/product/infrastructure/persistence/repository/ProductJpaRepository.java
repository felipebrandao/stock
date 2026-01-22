package br.com.felipebrandao.stock.product.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

    boolean existsByNameIgnoreCaseAndCategory_Id(String name, UUID categoryId);
}
