package kr.hhplus.be.server.web.coupon.contorller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.web.ApiResult;
import kr.hhplus.be.server.web.coupon.request.CouponRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coupon")
@Tag(name = "쿠폰 API")
@RequiredArgsConstructor
public class CouponController {

    private final CouponFacade couponFacade;

    @PostMapping("/send")
    public ApiResult<Void> sendCoupon( @Parameter(hidden = true) @RequestBody CouponRequest couponRequest) {
        couponFacade.createCoupon(new CouponCommand(couponRequest.getUserId(), couponRequest.getCouponId()));
        return ApiResult.success();
    }
}
