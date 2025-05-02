package kr.hhplus.be.server.domain.product;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {

    Optional<Product> findProductWithAllOptions(long productId);

    Optional<ProductOption> findOptionWithProduct(long productId, long optionId);

    Product save(Product product);

    ProductOption save(ProductOption option);
}
