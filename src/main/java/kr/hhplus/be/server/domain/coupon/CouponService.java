package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.Exception.CouponException.AlreadyIssuedCouponException;
import kr.hhplus.be.server.Exception.CouponException.CouponNotFoundException;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;


    public UserCoupon getValidUserCouponForOrder(long userId, long couponId) {

        UserCoupon userCoupon = couponRepository.findUserCouponsWithInfo(userId, couponId);

        if(userCoupon == null){
            throw new CouponNotFoundException();
        }

        UserCouponInfo userCouponInfo = new UserCouponInfo(userCoupon.getCoupon(), userCoupon);

        userCouponInfo.validateUsable();

        userCouponInfo.validateNotExpired();

       return userCoupon;
    }

    public void validateNotAlreadyIssued(long userId, long couponId){

        UserCoupon userCoupon = couponRepository.findUserCouponsWithInfo(userId, couponId);

        if (userCoupon != null) {
            throw new AlreadyIssuedCouponException();
        }

    }



    public void useCoupon(UserCoupon userCoupon){

        userCoupon.useCoupon();

    }

    public Coupon getCouponOrThrow(long couponId) {

        return couponRepository.findById(couponId).orElseThrow(CouponNotFoundException::new);

    }


    public Coupon validateCouponIssue(CouponCommand couponCommand) {
        Coupon coupon = couponRepository.findByIdWithLock(couponCommand.getCouponId()).orElseThrow(CouponNotFoundException::new);

        // 사용자 쿠폰 관계 검증
        validateNotAlreadyIssued(couponCommand.getUserId(), couponCommand.getCouponId());

        // 쿠폰 수량 검증 및 재고 업데이트
        coupon.validateAndDecreaseLeftQty();

        return coupon;

    }

    public void createUserCoupon(CouponCommand couponCommand) {

        UserCoupon userCoupon = UserCoupon.create(new User(couponCommand.userId), new Coupon(couponCommand.couponId));

        couponRepository.save(userCoupon);


    }


}
