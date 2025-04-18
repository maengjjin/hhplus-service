package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.Exception.ProductException.ProductNotFoundException;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductOption;
import kr.hhplus.be.server.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    private final ProductOptionJpaRepository productOptionJpaRepository;


    @Override
    public Product findProductWithOptions(long productId) {

        return productJpaRepository.findProductWithOptions(productId).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public ProductOption findOptionWithProduct(long productId, long optionId) {

        return productOptionJpaRepository.findOptionWithProduct(productId, optionId)
            .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public void updateStockQuantity(long productId, long optionId, long stockQty) {
        productOptionJpaRepository.updateStockQuantity(productId, optionId, stockQty);
    }
}
