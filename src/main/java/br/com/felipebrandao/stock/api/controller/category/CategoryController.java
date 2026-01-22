package br.com.felipebrandao.stock.api.controller.category;

import br.com.felipebrandao.stock.api.controller.category.dto.request.CreateCategoryRequest;
import br.com.felipebrandao.stock.api.controller.category.dto.response.CategoryResponse;
import br.com.felipebrandao.stock.category.application.usecase.CreateCategoryUseCase;
import br.com.felipebrandao.stock.category.application.usecase.DeleteCategoryUseCase;
import br.com.felipebrandao.stock.category.application.usecase.GetCategoryUseCase;
import br.com.felipebrandao.stock.category.application.usecase.ListCategoriesUseCase;
import br.com.felipebrandao.stock.category.domain.model.Category;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;
    private final GetCategoryUseCase getCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CreateCategoryRequest request) {
        UUID id = createCategoryUseCase.execute(request.getName());
        Category created = getCategoryUseCase.execute(id);
        return ResponseEntity.ok(toResponse(created));
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> list(Pageable pageable) {
        Page<CategoryResponse> response = listCategoriesUseCase.execute(pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(getCategoryUseCase.execute(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteCategoryUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .build();
    }
}
