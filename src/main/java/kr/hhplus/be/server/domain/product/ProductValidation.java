package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.Exception.ProductException.OutOfStockException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductValidation {

    private final long productId;

    private final long optionId;

    private final long stockQty;

    private final long orderQty;

    private final ProductStatus status;

    private final long price;


    void stockValidation(long qty){
        if(qty > stockQty) {
            throw new OutOfStockException();
        }
    }


    public ProductValidation decreaseStock() {
        return new ProductValidation(
            productId, optionId, stockQty - orderQty, orderQty, status, price
        );
    }

    public ProductValidation of(ProductValidation base, long orderQty) {
        return new ProductValidation(
            base.getProductId(),
            base.getOptionId(),
            base.getStockQty(),
            orderQty,
            base.getStatus(),
            base.getPrice()
        );
    }

}
