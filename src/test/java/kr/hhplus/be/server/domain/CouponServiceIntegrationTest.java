package kr.hhplus.be.server.domain;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kr.hhplus.be.server.Exception.CouponException.CouponNotFoundException;
import kr.hhplus.be.server.Exception.ProductException.ProductNotFoundException;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CouponServiceIntegrationTest {

    @Autowired
    CouponService couponService;


    @Autowired
    CouponRepository couponRepository;

    @Test
    void 쿠폰_조회_성공() {
        // given
        long userId = 1L;
        long couponId = 1L;

        // when
        UserCouponInfo result = couponService.findUserCoupon(userId, couponId);

        // then
        assertThat(result.getCouponId()).isEqualTo(couponId);
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getCouponYn()).isEqualTo("N");
    }


    @Test
    void 쿠폰_조회_실패() {
        // given
        long userId = 20L;
        long couponId = 1L;

        // when then
        assertThrows(CouponNotFoundException.class, () ->
            couponService.findUserCoupon(userId, couponId)
        );
    }


    @Test
    void 쿠폰_사용_시_DB_업데이트_확인() {
        // given 쿠폰 사용자 정보 확인
        long userId = 1L;
        long couponId = 1L;

        UserCouponInfo couponInfo = couponService.findUserCoupon(userId, couponId);

        // when
        couponService.useCoupon(couponInfo);


        // then DB에서 조회해서 업데이트 됐는지 검증
        UserCoupon userCoupon = couponRepository.findUserCouponsWithInfo(userId, couponId);

        assertThat(userCoupon.getCouponYn()).isEqualTo("Y");
    }


}
