package kr.hhplus.be.server.domain.product;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {

    Product findProductWithOptions(long productId);

    ProductOption findOptionWithProduct(long productId, long optionId);

    void updateStockQuantity(long productId, long optionId, long stockQty);
}
