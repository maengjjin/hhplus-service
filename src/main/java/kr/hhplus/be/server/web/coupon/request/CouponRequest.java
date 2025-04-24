package kr.hhplus.be.server.web.coupon.request;

import kr.hhplus.be.server.domain.coupon.CouponCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CouponRequest {

    long userId;

    long couponId;


}
