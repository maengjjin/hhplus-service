package kr.hhplus.be.server.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.Exception.CouponException.CouponMinimumAmountNotMetException;
import kr.hhplus.be.server.Exception.PointException.InsufficientPointBalanceException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.UserCouponInfo;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderCommand.OrderItemDetail;
import kr.hhplus.be.server.domain.order.OrderPaymentCalculator;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


public class OrderPaymentCalculatorTest {

    List<OrderItemDetail> items;

    Coupon coupon;

    User user;

    UserCouponInfo userCouponInfo;

    UserCoupon userCoupon;




    @BeforeEach
    void beforeEach(){

        items = List.of(
            new OrderCommand.OrderItemDetail(1L, 100L, 1, 30000L),
            new OrderCommand.OrderItemDetail(1L, 102L, 1, 30000L),
            new OrderCommand.OrderItemDetail(3L, 101L, 3, 30000L)
        );

        coupon = new Coupon(101L,
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

        user = User.of(1L, 500_000L);

        userCoupon = new UserCoupon(1000L, user, coupon, "N", LocalDateTime.now(), null);

        userCouponInfo = new UserCouponInfo(coupon, userCoupon);


    }

    @Test
    void 주문금액_정상_계산_성공() {
        // given 사용자 포인트와 쿠폰, 상품 목록
        OrderPaymentCalculator calculator = new OrderPaymentCalculator();

        // when 결제 금액 계산
        OrderPaymentCalculator.PriceSummary summary = calculator.calculateOrderAmount(user.getPoint(), userCoupon, items);

        // then 상품 가격, 쿠폰 할인, 총 결제 금액이 올바르게 계산되었는지 검증
        assertThat(summary.getProductPrice()).isEqualTo(150_000L);
        assertThat(summary.getCouponPrice()).isEqualTo(5000L);
        assertThat(summary.getTotalPrice()).isEqualTo(145_000L);
    }

    @Test
    void 쿠폰_적용_최소금액_미만일_경우_예외발생() {
        // given 상품 총액이 최소 할인 조건 미만
        OrderPaymentCalculator calculator = new OrderPaymentCalculator();
        items = List.of(
            new OrderCommand.OrderItemDetail(1L, 100L, 1, 1000L),
            new OrderCommand.OrderItemDetail(1L, 102L, 1, 1000L),
            new OrderCommand.OrderItemDetail(3L, 101L, 3, 1000L)
        );
        long point = 5000L;

        // then 최소 금액 미만으로 예외 발생
        assertThatThrownBy(() -> calculator.calculateOrderAmount(point, userCoupon, items))
            .isInstanceOf(CouponMinimumAmountNotMetException.class);
    }

    @Test
    void 포인트가_결제금액보다_적을_경우_예외발생() {
        // given 사용자 포인트가 부족한 상황
        OrderPaymentCalculator calculator = new OrderPaymentCalculator();
        long point = 5000L; // 총 결제금액보다 적음

        // then 포인트 부족 예외 발생
        assertThatThrownBy(() -> calculator.calculateOrderAmount(point, userCoupon, items))
            .isInstanceOf(InsufficientPointBalanceException.class);
    }

}
