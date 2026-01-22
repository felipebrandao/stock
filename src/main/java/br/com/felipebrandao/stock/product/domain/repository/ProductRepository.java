package br.com.felipebrandao.stock.product.domain.repository;

import br.com.felipebrandao.stock.product.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(UUID id);

    /** Retorna todos (sem paginação). */
    List<Product> findAll();

    /** Retorna paginado (recomendado para endpoints GET). */
    Page<Product> findAll(Pageable pageable);

    void deleteById(UUID id);

    boolean existsByNameAndCategoryId(String name, UUID categoryId);
}
