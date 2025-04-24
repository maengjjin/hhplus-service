package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.Exception.CouponException.CouponNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public UserCouponInfo findUserCoupon(long userId, long couponId) {

        UserCouponInfo userCoupon = couponRepository.findUserCouponsWithInfo(userId, couponId);

       if(userCoupon == null){
            throw new CouponNotFoundException();
       }

       return userCoupon;
    }
}
