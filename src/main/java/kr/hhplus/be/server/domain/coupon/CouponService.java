package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.Exception.CouponException.CouponNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public UserCouponInfo findUserCoupon(long userId, long couponId) {

        UserCoupon userCoupon = couponRepository.findUserCouponsWithInfo(userId, couponId);

        UserCouponInfo userCouponInfo = new UserCouponInfo(userCoupon.getCoupon(), userCoupon);

       if(userCoupon == null){
            throw new CouponNotFoundException();
       }

        userCouponInfo.validateUsable();

        userCouponInfo.validateNotExpired();

       return userCouponInfo;
    }



    public void useCoupon(UserCouponInfo userCouponInfo){

        couponRepository.updateCouponUsed(userCouponInfo.getUserId(), userCouponInfo.getCouponId());


    }

}
