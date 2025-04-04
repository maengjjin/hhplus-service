package kr.hhplus.be.server.user.controller;

import java.util.List;
import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.coupon.response.CouponResponse;
import kr.hhplus.be.server.user.response.UserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("{/{userId}}")
    public ApiResponse<UserResponse> getUserPoint(@PathVariable long userId) {
        return ApiResponse.success(UserResponse.of(userId, 10000L));
    }

    @GetMapping("/coupon/{userId}")
    public ApiResponse<List<CouponResponse>> getUserCoupon(@PathVariable long userId) {
        return ApiResponse.success(List.of(
            CouponResponse.of(1, "5,000원 할인쿠폰", 5000L, "20250501"),
            CouponResponse.of(2, "1,000원 할인쿠폰", 5000L, "20251001")
        ));
    }

}
