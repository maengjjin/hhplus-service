package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponFacade {

    @Autowired
    private UserService userService;

    @Autowired
    private CouponService couponService;

    @Transactional
    public void createCoupon(CouponCommand couponCommand) {

        // 사용자 확인
        userService.getUserInfo(couponCommand.getUserId());

        // 쿠폰 자체 검증
        couponService.validateCouponIssue(couponCommand);

        // 유저 쿠폰발급 저장
        couponService.createUserCoupon(couponCommand);


    }

}
