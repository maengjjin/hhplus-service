package kr.hhplus.be.server.domain;

import static kr.hhplus.be.server.domain.coupon.CouponType.FIXED;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import kr.hhplus.be.server.Exception.CouponException.CouponNotFoundException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponInfo;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CouponServiceTest {

    @InjectMocks
    CouponService couponService;

    @Mock
    CouponRepository couponRepository;

    long userId = 1L;
    long couponId = 1000L;

    UserCouponInfo userCouponInfo;

    UserCoupon userCoupon;

    @BeforeEach
    void beforeEach(){

        Coupon coupon = new Coupon(101L,
            "5천원 할인 쿠폰",
            CouponType.FIXED,
            0,
            5000,
            10000,
            5000,
            1000,
            999,
            LocalDateTime.of(2025, 5, 1, 23, 59) // expiresAt
        );

        User user = new User(1L);

        userCoupon = new UserCoupon(1000L, user, coupon, "N", LocalDateTime.now(), null);


        userCouponInfo = new UserCouponInfo(coupon, userCoupon);

    }

    @Test
    void 사용자_쿠폰_정보_조회(){

        // given 사용자 쿠폰 설정
        when(couponRepository.findUserCouponsWithInfo(userId,couponId)).thenReturn(userCoupon);

        // when 쿠폰 조회
        UserCouponInfo result = couponService.findUserCoupon(userId, couponId);

        // then 검증
        verify(couponRepository, times(1)).findUserCouponsWithInfo(userId, couponId);

    }

    @Test
    void 유저쿠폰조회예외검증(){


        when(couponRepository.findUserCouponsWithInfo(userId,couponId)).thenReturn(null);

        Assertions.assertThrows(CouponNotFoundException.class, ()->
            couponService.findUserCoupon(userId, couponId)
            );

    }



}
