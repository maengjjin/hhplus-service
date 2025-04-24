package kr.hhplus.be.server.product.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {

    private long productId;

    private String name;

    private long price;

    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProductOptionResponse> options;

    public static ProductResponse of(long productId, String name, long price, String status, List<ProductOptionResponse> optionList) {
        return new ProductResponse(productId, name, price, status, optionList);
    }

    public static ProductResponse of(long productId, String name, long price, String status) {
        return new ProductResponse(productId, name, price, status, null);
    }


}
