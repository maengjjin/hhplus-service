package kr.hhplus.be.server.domain.product;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductInfo {

    private long productId;

    private String name;

    private long price;

    private ProductStatus status;

    private List<ProductOption> options;


    @Builder
    public ProductInfo(Product product, List<ProductOption> options) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.status = product.getStatus();
        this.options = options;
    }



}
