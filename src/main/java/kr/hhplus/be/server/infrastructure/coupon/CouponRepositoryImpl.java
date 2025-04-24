package kr.hhplus.be.server.infrastructure.coupon;

import java.util.Optional;
import kr.hhplus.be.server.Exception.CouponException.CouponNotFoundException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository{

    private final UserCouponJpaRepository userCouponJpaRepository;
    private final CouponJpaRepository couponJpaRepository;

    @Override
    public UserCoupon findUserCouponsWithInfo(long userId, long couponId) {
        return userCouponJpaRepository.findWithCouponByUserIdAndCouponId(userId, couponId);
    }



    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }

    @Override
    public UserCoupon save(UserCoupon userCoupon) {
        return userCouponJpaRepository.save(userCoupon);
    }

    @Override
    public Optional<Coupon> findById(long couponId) {
        return couponJpaRepository.findById(couponId);
    }
}
