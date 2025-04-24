package kr.hhplus.be.server.web.product.response;

import kr.hhplus.be.server.domain.product.ProductOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductOptionResponse {

    private long optionId;

    private String name;

    private long price;

    private long stockQty;

    // ✅ 여기가 핵심!
    public static ProductOptionResponse from(ProductOption option) {
        return new ProductOptionResponse(
            option.getOptionId(),
            option.getOptionName(),
            option.getPrice(),
            option.getStockQty()
        );
    }
}

