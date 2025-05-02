package kr.hhplus.be.server.domain.product;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class ProductDTO {

    @Getter
    public static class ProductOptionResult {

        private final long productId;

        private final String name;

        private final long price;

        private final ProductStatus status;

        private final List<ProductOption> options;


        @Builder
        public ProductOptionResult(Product product, List<ProductOption> options) {
            this.productId = product.getProductId();
            this.name = product.getName();
            this.price = product.getPrice();
            this.status = product.getStatus();
            this.options = options;
        }
    }



    @Getter
    public static class ProductOrderResult {

        private final long productId;

        private final long optionId;

        private final long stockQty;

        private final long orderQty;

        private final ProductStatus status;

        private final long price;

        private final String productName;

        private final String optionName;


        public ProductOrderResult(long productId, long optionId, long stockQty, long orderQty,
            ProductStatus status, long price, String productName, String optionName) {
            this.productId = productId;
            this.optionId = optionId;
            this.stockQty = stockQty;
            this.orderQty = orderQty;
            this.status = status;
            this.price = price;
            this.productName = productName;
            this.optionName = optionName;
        }
    }

}
