//package kr.hhplus.be.server.infrastructure.coupon;
//
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.verify;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import java.time.LocalDateTime;
//import kr.hhplus.be.server.domain.coupon.Coupon;
//import kr.hhplus.be.server.domain.coupon.CouponType;
//import kr.hhplus.be.server.domain.coupon.UserCoupon;
//import kr.hhplus.be.server.domain.user.User;
//import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.transaction.annotation.Transactional;
//
//
//@Transactional
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class UserCouponJpaRepositoryTest {
//
//
//    @Autowired
//    private UserCouponJpaRepository userCouponJpaRepository;
//
//    @Autowired
//    private UserJpaRepository userJpaRepository;
//
//    @Autowired
//    private CouponJpaRepository couponJpaRepository;
//
//    @PersistenceContext // ✅ 영속성 컨텍스트 주입
//    private EntityManager em;
//
//
//
//    @Test
//    void 사용자에게_해당_쿠폰이_있을때_조회_성공() {
//        // given 사용자와 쿠폰을 저장하고 사용자 쿠폰도 저장함
//        User user = userJpaRepository.save(User.builder().point(10000L).build());
//        Coupon coupon = couponJpaRepository.save(new Coupon(
//            "1000원 할인 쿠폰",
//            CouponType.FIXED,
//            0, 3000, 1000, 3000,
//            100, 100,
//            LocalDateTime.now().plusDays(30)
//        ));
////        userCouponJpaRepository.save(new UserCoupon(user.getUserId(), coupon, "N"));
//
//        // when 사용자 ID와 쿠폰 ID로 사용자 쿠폰을 조회
//        UserCoupon result = userCouponJpaRepository.findWithCouponByUserIdAndCouponId(user.getUserId(), coupon.getCouponId());
//
//        // then 사용자 쿠폰이 정상적으로 조회되고, 쿠폰 정보도 일치해야 함
//        assertThat(result).isNotNull();
//        assertThat(result.getCoupon().getCouponId()).isEqualTo(coupon.getCouponId());
//    }
//
//    @Test
//    void 사용자가가_해당쿠폰이_없을때_null_확인() {
//        // given 사용자와 쿠폰을 저장하고 사용자 쿠폰도 저장함
//        User user = userJpaRepository.save(User.builder().point(10000L).build());
//        Coupon coupon = couponJpaRepository.save(new Coupon(
//            "1000원 할인 쿠폰",
//            CouponType.FIXED,
//            0, 3000, 1000, 3000,
//            100, 100,
//            LocalDateTime.now().plusDays(30)
//        ));
////        userCouponJpaRepository.save(new UserCoupon(user.getUserId(), coupon, "N"));
//
//        // when 존재하지 않는 쿠폰 ID로 사용자 쿠폰 조회
//        UserCoupon result = userCouponJpaRepository.findWithCouponByUserIdAndCouponId(user.getUserId(), 1000L);
//
//        // then 검증
//        assertThat(result).isNull();
//    }
//
//}
