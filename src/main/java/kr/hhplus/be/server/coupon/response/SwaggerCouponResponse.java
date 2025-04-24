package kr.hhplus.be.server.coupon.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SwaggerCouponResponse {

    @Schema(description = "code", example = "200")
    private int code;

    @Schema(description = "message", example = "success")
    private String message;

    @Schema(description = "쿠폰 목록",
        example = "["
            + "{\"couponId\":1,\"name\":\"5,000원 할인쿠폰\",\"discountAmount\":5000,\"expiresAt\":\"20250501\"}, "
            + "{\"couponId\":2,\"name\":\"1,000원 할인쿠폰\",\"discountAmount\":5000,\"expiresAt\":\"20251001\"}"
            + "]")
    private List<CouponResponse> data;

}
