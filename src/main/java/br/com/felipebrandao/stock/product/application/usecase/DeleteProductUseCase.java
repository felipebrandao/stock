package br.com.felipebrandao.stock.product.application.usecase;

import br.com.felipebrandao.stock.product.domain.repository.ProductRepository;
import br.com.felipebrandao.stock.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final ProductRepository productRepository;

    public void execute(UUID id) {
        if (productRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Produto não encontrado");
        }
        productRepository.deleteById(id);
    }
}
