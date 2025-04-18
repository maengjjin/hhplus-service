package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.Exception.CouponException.CouponNotFoundException;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

    private final UserCouponJpaRepository userCouponJpaRepository;


    @Override
    public UserCoupon findUserCouponsWithInfo(long userId, long couponId) {
        return userCouponJpaRepository.findWithCouponByUserIdAndCouponId(userId, couponId).orElseThrow(CouponNotFoundException::new);
    }

    @Override
    public void updateCouponUsed(long userId, long couponId) {
        userCouponJpaRepository.updateCouponUsed(userId, couponId);
    }
}
