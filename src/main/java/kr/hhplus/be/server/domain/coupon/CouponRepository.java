package kr.hhplus.be.server.domain.coupon;

import org.springframework.stereotype.Component;

@Component
public interface CouponRepository {

    UserCoupon findUserCouponsWithInfo(long userId, long couponId);

    void updateCouponUsed(long userId, long couponId);
}
