package kr.hhplus.be.server.domain.product;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository {

    ProductInfo findProductWithOptions(long productId);

    ProductValidation fetchOptionByProductId(ProductCommand orderItem);

    void updateStockQuantity(ProductValidation stockOrder);
}
