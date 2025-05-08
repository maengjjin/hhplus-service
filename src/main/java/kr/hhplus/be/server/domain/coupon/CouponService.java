package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.Exception.CouponException.AlreadyIssuedCouponException;
import kr.hhplus.be.server.Exception.CouponException.CouponNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;


    public UserCoupon getValidUserCouponForOrder(long userId, long couponId) {

        UserCoupon userCoupon = couponRepository.findUserCouponsWithInfo(userId, couponId);

        if(userCoupon == null){
            throw new CouponNotFoundException();
        }

        userCoupon.validateUsable();

        userCoupon.validateNotExpired();

       return userCoupon;
    }


    public void useCoupon(UserCoupon userCoupon){

        userCoupon.useCoupon();

    }

    public Coupon getCouponOrThrow(long couponId) {

        return couponRepository.findById(couponId).orElseThrow(CouponNotFoundException::new);

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void couponIssue(CouponCommand couponCommand) {

        Coupon coupon = couponRepository.findByIdWithLock(couponCommand.getCouponId()).orElseThrow(CouponNotFoundException::new);

        // 사용자 쿠폰 관계 검증
        UserCoupon existingUserCoupon  = couponRepository.findUserCouponsWithInfo(couponCommand.getUserId(), couponCommand.getCouponId());

        if (existingUserCoupon != null) {
            throw new AlreadyIssuedCouponException();
        }

        // 쿠폰 수량 검증 및 재고 업데이트
        coupon.validateAndDecreaseLeftQty();

        // 사용자 쿠폰 발급
        couponRepository.save(UserCoupon.create(couponCommand.getUserId(), coupon));

    }


}
