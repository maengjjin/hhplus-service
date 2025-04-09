package kr.hhplus.be.server.web.user;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.hhplus.be.server.web.coupon.response.CouponResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SwaggerUserCouponResponseResult {


    @Schema(description = "code", example = "200")
    private int code;

    @Schema(description = "message", example = "success")
    private String message;

    @Schema(
        description = "쿠폰 목록",
        example = "[" +
            "{\"couponId\":1,\"name\":\"5,000원 할인쿠폰\",\"discountAmount\":5000,\"expiresAt\":\"20250501\"}," +
            "{\"couponId\":2,\"name\":\"1,000원 할인쿠폰\",\"discountAmount\":1000,\"expiresAt\":\"20251001\"}" +
            "]"
    )
    private CouponResponse data;
}
