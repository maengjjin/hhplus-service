package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.Exception.ProductException.OutOfStockException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductValidation {

    private long productId;

    private long optionId;

    private long stockQty;

    private ProductStatus status;

    private long price;


    void stockValidation(long qty){
        if(qty > stockQty) {
            throw new OutOfStockException();
        }
    }



}
