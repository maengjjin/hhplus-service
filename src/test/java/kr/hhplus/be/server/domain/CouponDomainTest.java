package kr.hhplus.be.server.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import kr.hhplus.be.server.Exception.CouponException.CouponAlreadyUsedException;
import kr.hhplus.be.server.Exception.CouponException.CouponNotFoundException;
import kr.hhplus.be.server.Exception.CouponException.CouponOutOfStockException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponInfo;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CouponDomainTest {


    Coupon coupon;




    @BeforeEach
    void beforeEach(){

        coupon = new Coupon(101L,
            "5천원 할인 쿠폰",
            CouponType.FIXED,
            0,
            5000,
            10000,
            5000,
            1000,
            1,
            LocalDateTime.of(2025, 5, 1, 23, 59) // expiresAt
        );


    }


    @Test
    void 쿠폰_발급을_위한_수량이_없을때_실패() {
        // given 수량이 0인 쿠폰
        Coupon coupon = new Coupon(
            101L,
            "5천원 할인 쿠폰",
            CouponType.FIXED,
            0,
            5000,
            10000,
            5000,
            1000,
            0, // leftQty = 0
            LocalDateTime.of(2025, 5, 1, 23, 59)
        );

        // when & then 수량 부족 예외 발생
        assertThrows(CouponOutOfStockException.class, () -> {
            coupon.validateAndDecreaseLeftQty();
        });
    }

    @Test
    void 쿠폰_발급을_위한_수량_검증_성공() {


        // when & then 수량 감소 로직 수행 시 예외 없음
        assertDoesNotThrow(() -> {
            coupon.validateAndDecreaseLeftQty();
        });
    }



}
