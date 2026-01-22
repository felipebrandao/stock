package br.com.felipebrandao.stock.api.controller.product;

import br.com.felipebrandao.stock.api.controller.product.dto.request.CreateProductRequest;
import br.com.felipebrandao.stock.api.controller.product.dto.response.ProductResponse;
import br.com.felipebrandao.stock.product.application.usecase.CreateProductUseCase;
import br.com.felipebrandao.stock.product.application.usecase.DeleteProductUseCase;
import br.com.felipebrandao.stock.product.application.usecase.GetProductUseCase;
import br.com.felipebrandao.stock.product.application.usecase.ListProductsUseCase;
import br.com.felipebrandao.stock.product.domain.model.Product;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final ListProductsUseCase listProductsUseCase;
    private final GetProductUseCase getProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        UUID id = createProductUseCase.execute(request.getName(), request.getCategoryId());
        Product created = getProductUseCase.execute(id);
        return ResponseEntity.ok(toResponse(created));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> list(Pageable pageable) {
        Page<ProductResponse> response = listProductsUseCase.execute(pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(getProductUseCase.execute(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteProductUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .categoryId(product.getCategoryId())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
