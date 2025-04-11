package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.point.PointHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository {

    void updatePoint(long userId, long amount);

    void savePointHistory(PointHistory entity);


    UserCouponInfo findUserCouponsWithInfo(long userId, long couponId);
}
