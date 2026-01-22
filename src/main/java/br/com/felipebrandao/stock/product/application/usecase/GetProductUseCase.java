package br.com.felipebrandao.stock.product.application.usecase;

import br.com.felipebrandao.stock.product.domain.model.Product;
import br.com.felipebrandao.stock.product.domain.repository.ProductRepository;
import br.com.felipebrandao.stock.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetProductUseCase {

    private final ProductRepository productRepository;

    public Product execute(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
    }
}
