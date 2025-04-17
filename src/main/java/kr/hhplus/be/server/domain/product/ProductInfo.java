package kr.hhplus.be.server.domain.product;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductInfo {

    private final long productId;

    private final String name;

    private final long price;

    private final ProductStatus status;

    private final List<ProductOption> options;


    @Builder
    public ProductInfo(Product product, List<ProductOption> options) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.status = product.getStatus();
        this.options = options;
    }



}
