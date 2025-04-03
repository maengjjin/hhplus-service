package kr.hhplus.be.server.product.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductOptionResponse {

    private long optionId;
    private String name;
    private long price;
    private long stockQty;

    public static ProductOptionResponse of(long optionId, String name, long price, long stockQty) {
        return new ProductOptionResponse(optionId, name, price, stockQty);
    }
}

