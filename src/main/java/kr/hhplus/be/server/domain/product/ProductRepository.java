package kr.hhplus.be.server.domain.product;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {

    Optional<Product> findProductWithOptions(long productId);

    Optional<ProductOption> findOptionWithProduct(long productId, long optionId);

    void updateStockQuantity(long productId, long optionId, long stockQty);

    Product save(Product product);

    ProductOption save(ProductOption option);

    int updateStockQuantity2(long productId, long optionId, long stockQty);
}
