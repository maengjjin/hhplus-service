package kr.hhplus.be.server.coupon.contorller;

import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.coupon.response.CouponResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @PostMapping("send/{couponId}")
    public ApiResponse<CouponResponse> sendCoupon(@PathVariable long couponId) {
        return ApiResponse.success(CouponResponse.of(
            1, "5,000원 할인쿠폰", 5000L, "20250501")
        );
    }
}
