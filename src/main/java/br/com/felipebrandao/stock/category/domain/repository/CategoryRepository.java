package br.com.felipebrandao.stock.category.domain.repository;

import br.com.felipebrandao.stock.category.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository {
    Category save(Category category);

    Optional<Category> findById(UUID id);

    /** Retorna todos (sem paginação). */
    List<Category> findAll();

    /** Retorna paginado (recomendado para endpoints GET). */
    Page<Category> findAll(Pageable pageable);

    void deleteById(UUID id);

    boolean existsByName(String name);
}
