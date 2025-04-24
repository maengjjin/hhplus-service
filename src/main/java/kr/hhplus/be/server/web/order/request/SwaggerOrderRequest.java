package kr.hhplus.be.server.web.order.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import kr.hhplus.be.server.web.product.response.ProductOptionResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SwaggerOrderRequest {

    @Schema(description = "상품 ID", example = "101")
    private int userId;

    @Schema(description = "옵션 ID", example = "201")
    private int couponId;

    @Schema(
        description = "주문 상품 리스트",
        example = "["
            + "{\"productId\":101,\"optionId\":201,\"productPrice\":5000,\"orderQty\":2},"
            + "{\"productId\":102,\"optionId\":202,\"productPrice\":19000,\"orderQty\":1}"
            + "]"
    )
    private List<ProductOptionResponse> data;


}
