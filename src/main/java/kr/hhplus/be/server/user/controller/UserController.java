package kr.hhplus.be.server.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.hhplus.be.server.common.ApiResult;
import kr.hhplus.be.server.coupon.response.CouponResponse;
import kr.hhplus.be.server.coupon.response.SwaggerCouponResponse;
import kr.hhplus.be.server.user.response.SwaggerUserResponseResult;
import kr.hhplus.be.server.user.response.UserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "사용자 API")
public class UserController {


    @Operation(summary = "사용자 포인트 조회 API", description = "사용자의 보유 쿠폰을 조회합니다",
        responses = @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SwaggerUserResponseResult.class))))
    @Parameters({ @Parameter(name = "userId", description = "사용자 id", required = true) })
    @GetMapping("/point/{userId}")
    public ApiResult<UserResponse> getUserPoint(@Parameter(hidden = true) @PathVariable long userId) {
        return ApiResult.success(UserResponse.of(userId, 10000L));
    }



    @Operation(summary = "사용자 보유 쿠폰 조회 API", description = "사용자의 보유 쿠폰을 조회합니다",
        responses = @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = SwaggerCouponResponse.class))))
    @Parameters({ @Parameter(name = "userId", description = "사용자 id", required = true)})
    @GetMapping("/coupon/{userId}")
    public ApiResult<List<CouponResponse>> getUserCoupon(@Parameter(hidden = true) @PathVariable long userId) {
        return ApiResult.success(List.of(
            CouponResponse.of(1, "5,000원 할인쿠폰", 5000L, "20250501"),
            CouponResponse.of(2, "1,000원 할인쿠폰", 5000L, "20251001")
        ));
    }

}
