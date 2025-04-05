package kr.hhplus.be.server.product.response;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.coupon.response.CouponResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SwaggerProductResponse {

    @Schema(description = "code", example = "200")
    private int code;

    @Schema(description = "message", example = "success")
    private String message;

    @Schema(
        description = "상품 목록",
        example = "[" +
            "{\"productId\":1,\"name\":\"후드티\",\"price\":30000,\"status\":\"상품 판매\"}," +
            "{\"productId\":2,\"name\":\"맨투맨\",\"price\":35000,\"status\":\"상품 판매\"}," +
            "{\"productId\":3,\"name\":\"블라우스\",\"price\":28000,\"status\":\"상품 판매\"}," +
            "{\"productId\":4,\"name\":\"신발\",\"price\":32000,\"status\":\"상품 판매\"}," +
            "{\"productId\":5,\"name\":\"슬리퍼\",\"price\":27000,\"status\":\"상품 판매\"}" +
            "]"
    )
    private CouponResponse data;

}
