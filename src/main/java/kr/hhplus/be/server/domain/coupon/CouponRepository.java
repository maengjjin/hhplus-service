package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public interface CouponRepository {

    UserCoupon findUserCouponsWithInfo(long userId, long couponId);

    Coupon save(Coupon coupon);

    UserCoupon save(UserCoupon userCoupon);

    Optional<Coupon> findById(long couponId);

}
