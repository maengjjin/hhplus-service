package kr.hhplus.be.server.infrastructure.product;

import java.util.Optional;
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
    public Optional<Product> findProductWithOptions(long productId) {

        return productJpaRepository.findProductWithOptions(productId);
    }

    @Override
    public Optional<ProductOption>  findOptionWithProduct(long productId, long optionId) {

        return productOptionJpaRepository.findOptionWithProduct(productId, optionId);
    }

    @Override
    public void updateStockQuantity(long productId, long optionId, long stockQty) {
        productOptionJpaRepository.updateStockQuantity(productId, optionId, stockQty);
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public ProductOption save(ProductOption option) {
        return productOptionJpaRepository.save(option);
    }

    @Override
    public int updateStockQuantity2(long productId, long optionId, long stockQty) {
        return  productOptionJpaRepository.updateStockQuantity2(productId, optionId, stockQty);
    }


}
