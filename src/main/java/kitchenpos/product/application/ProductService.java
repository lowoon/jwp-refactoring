package kitchenpos.product.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Products;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional
    public Product create(final Product product) {
        return productRepository.save(product);
    }

    public Products findAllByIdIn(final List<Long> ids) {
        List<Product> products = productRepository.findAllByIdIn(ids);
        if (products.size() != ids.size()) {
            throw new IllegalArgumentException();
        }
        return new Products(products);
    }

    public List<Product> list() {
        return productRepository.findAll();
    }
}
