package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.point.PointHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {

    UserCouponInfo findUserCouponsWithInfo(long userId, long couponId);
}
