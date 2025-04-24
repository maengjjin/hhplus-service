package kr.hhplus.be.server.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;
import kr.hhplus.be.server.Exception.CouponException.CouponOutOfStockException;
import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CouponFasadeIntegrationTest  extends DatabaseConnectionTest  {


    @Autowired
    UserRepository userRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    CouponFacade couponFacade;




    @Test
    void 쿠폰_발급_성공() {
        // given 사용자와 쿠폰 발급 가능한 쿠폰 세팅
        User user = userRepository.save(User.builder().point(3000L).build());

        Coupon coupon = new Coupon(
            "5천원 할인 쿠폰",
            CouponType.FIXED,
            0,
            5000,
            10000,
            5000,
            1000,
            999, // 수량 충분
            LocalDateTime.of(2025, 5, 1, 23, 59)
        );
        couponRepository.save(coupon);

        CouponCommand couponCommand = new CouponCommand(user.getUserId(), coupon.getCouponId());

        // when 쿠폰 발급 실행
        couponFacade.createCoupon(couponCommand);

        // then 1) 사용자 쿠폰이 정상적으로 저장되었는지 확인
        UserCoupon userCoupon = couponRepository.findUserCouponsWithInfo(couponCommand.getUserId(), couponCommand.getCouponId());
        assertThat(userCoupon.getUser().getUserId()).isEqualTo(couponCommand.getUserId());
        assertThat(userCoupon.getCoupon().getCouponId()).isEqualTo(couponCommand.getCouponId());
        assertThat(userCoupon.getCouponYn()).isEqualTo("N");

        // then 2) 쿠폰 재고가 1 감소했는지 확인
        Optional<Coupon> updatedCoupon = couponRepository.findById(couponCommand.getCouponId());
        assertThat(updatedCoupon).isPresent();
        assertThat(updatedCoupon.get().getLeftQty()).isEqualTo(coupon.getLeftQty() - 1L);
    }

    @Test
    void 쿠폰_재고가_없으면_발급_예외발생() {
        // given 쿠폰 수량이 0인 상태
        Coupon coupon = new Coupon(
            "5천원 할인 쿠폰",
            CouponType.FIXED,
            0,
            5000,
            10000,
            5000,
            1000,
            0, // 재고 없음
            LocalDateTime.of(2025, 5, 1, 23, 59)
        );

        User user = userRepository.save(User.builder().point(3000L).build());
        couponRepository.save(coupon);
        CouponCommand command = new CouponCommand(user.getUserId(), coupon.getCouponId());

        // when & then 쿠폰 발급 시 예외 발생 확인
        assertThatThrownBy(() -> couponFacade.createCoupon(command))
            .isInstanceOf(CouponOutOfStockException.class);
    }

}
