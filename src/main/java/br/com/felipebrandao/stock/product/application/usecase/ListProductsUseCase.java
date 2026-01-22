package br.com.felipebrandao.stock.product.application.usecase;

import br.com.felipebrandao.stock.product.domain.model.Product;
import br.com.felipebrandao.stock.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListProductsUseCase {

    private final ProductRepository productRepository;

    public List<Product> execute() {
        return productRepository.findAll();
    }

    public Page<Product> execute(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
}
