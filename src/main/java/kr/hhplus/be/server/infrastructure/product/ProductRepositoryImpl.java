package kr.hhplus.be.server.infrastructure.product;

import java.util.Optional;
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
    public Optional<Product> findProductWithAllOptions(long productId) {

        return productJpaRepository.findProductWithAllOptions(productId);
    }

    @Override
    public Optional<ProductOption>  findOptionWithProduct(long productId, long optionId) {

        return productOptionJpaRepository.findOptionWithProduct(productId, optionId);
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
    public Optional<Product> findById(long productId) {
        return productJpaRepository.findById(productId);
    }


}
