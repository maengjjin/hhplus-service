package kr.hhplus.be.server.coupon.contorller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.ApiResult;
import kr.hhplus.be.server.coupon.response.CouponResponse;
import kr.hhplus.be.server.coupon.response.SwaggerCouponResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
@Tag(name = "쿠폰 API")
public class CouponController {

    @Operation(summary = "쿠폰 발급 API", description = "쿠폰을 사용자에게 발급합니다",
        responses = @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = SwaggerCouponResponse.class))))
    @Parameters({@Parameter(name = "couponId", description = "쿠폰 ID", required = true)})
    @PostMapping("/send/{couponId}")
    public ApiResult<CouponResponse> sendCoupon(
        @Parameter(hidden = true) @PathVariable long couponId) {
        return ApiResult.success(CouponResponse.of( 1, "5,000원 할인쿠폰", 5000L, "20250501")
        );
    }
}
