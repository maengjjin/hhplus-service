//package kr.hhplus.be.server.domain;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//import kr.hhplus.be.server.Exception.CouponException.AlreadyIssuedCouponException;
//import kr.hhplus.be.server.Exception.CouponException.CouponNotFoundException;
//import kr.hhplus.be.server.domain.coupon.Coupon;
//import kr.hhplus.be.server.domain.coupon.CouponCommand;
//import kr.hhplus.be.server.domain.coupon.CouponRepository;
//import kr.hhplus.be.server.domain.coupon.CouponService;
//import kr.hhplus.be.server.domain.coupon.CouponType;
//import kr.hhplus.be.server.domain.coupon.UserCoupon;
//import kr.hhplus.be.server.domain.user.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class CouponServiceTest {
//
//    @InjectMocks
//    CouponService couponService;
//
//    @Mock
//    CouponRepository couponRepository;
//
//    long userId = 1L;
//    long couponId = 1000L;
//
//    UserCouponInfo userCouponInfo;
//
//    UserCoupon userCoupon;
//
//    Coupon coupon;
//
//    @BeforeEach
//    void beforeEach(){
//
//        coupon = new Coupon(101L,
//            "5천원 할인 쿠폰",
//            CouponType.FIXED,
//            0,
//            5000,
//            10000,
//            5000,
//            1000,
//            999,
//            LocalDateTime.of(2025, 5, 1, 23, 59) // expiresAt
//        );
//
//        User user = new User(1L);
//
//        userCoupon = new UserCoupon(1000L, user.getUserId(), coupon, "N", LocalDateTime.now(), null);
//
//
//        userCouponInfo = new UserCouponInfo(coupon, userCoupon);
//
//
//
//    }
//
//    @Test
//    void 주문_시_사용자_쿠폰_정보_조회_성공() {
//        // given 사용자 쿠폰 정보가 존재함
//        when(couponRepository.findUserCouponsWithInfo(userId, couponId)).thenReturn(userCoupon);
//
//        // when 주문 시 사용자 쿠폰 정보 조회
//        UserCoupon result = couponService.getValidUserCouponForOrder(userId, couponId);
//
//        // then 쿠폰 정보가 정상적으로 조회됨
//        verify(couponRepository, times(1)).findUserCouponsWithInfo(userId, couponId);
//    }
//
//    @Test
//    void 주문_시_사용자_쿠폰_정보_조회_실패() {
//        // given 사용자 쿠폰 정보가 존재하지 않음
//        when(couponRepository.findUserCouponsWithInfo(userId, couponId)).thenReturn(null);
//
//        // when & then 예외 발생 검증
//        assertThrows(CouponNotFoundException.class, () -> {
//            couponService.getValidUserCouponForOrder(userId, couponId);
//        });
//    }
//
//    @Test
//    void 쿠폰이_존재할_때_조회_성공() {
//        // given 쿠폰이 존재함
//        when(couponRepository.findById(couponId)).thenReturn(Optional.ofNullable(coupon));
//
//        // when 쿠폰 조회
//        Coupon result = couponService.getCouponOrThrow(couponId);
//
//        // then 정상적으로 쿠폰이 반환됨
//        verify(couponRepository, times(1)).findById(couponId);
//        assertThat(result).isEqualTo(coupon);
//    }
//
//    @Test
//    void 쿠폰이_존재하지_않을_때_조회_실패() {
//        // given 쿠폰이 존재하지 않음
//        when(couponRepository.findById(couponId)).thenReturn(Optional.empty());
//
//        // when & then 쿠폰 미존재 예외 발생 검증
//        assertThrows(CouponNotFoundException.class, () -> {
//            couponService.getCouponOrThrow(couponId);
//        });
//    }
//
//    @Test
//    void 쿠폰_발급_시_사용자_쿠폰이_이미_존재할_때_실패() {
//        // given 이미 사용자에게 발급된 쿠폰이 존재함
//        when(couponRepository.findUserCouponsWithInfo(userId, couponId)).thenReturn(userCoupon);
//
//        // when & then 중복 발급 예외 발생 검증
//        assertThrows(AlreadyIssuedCouponException.class, () -> {
//            couponService.validateNotAlreadyIssued(userId, couponId);
//        });
//    }
//
//    @Test
//    void 쿠폰_발급_시_사용자_쿠폰이_존재하지_않을_때_성공() {
//        // given 사용자에게 발급된 쿠폰이 없음
//        when(couponRepository.findUserCouponsWithInfo(userId, couponId)).thenReturn(null);
//
//        // when 쿠폰 중복 발급 여부 확인
//        couponService.validateNotAlreadyIssued(userId, couponId);
//
//        // then 중복 아님으로 통과
//        verify(couponRepository, times(1)).findUserCouponsWithInfo(userId, couponId);
//    }
//
//    @Test
//    void 유저_쿠폰_발급_성공() {
//        // given 유저와 쿠폰 정보 세팅
//        when(couponRepository.findUserCouponsWithInfo(userId, couponId)).thenReturn(userCoupon);
//        CouponCommand couponCommand = new CouponCommand(userId, couponId);
//
//        // when 유저-쿠폰 발급 처리
//        couponService.createUserCoupon(couponCommand);
//
//        // then 유저 쿠폰 정상 저장 확인
//        UserCoupon userCoupon = couponRepository.findUserCouponsWithInfo(userId, couponId);
//        assertThat(userCoupon).isNotNull();
//        assertThat(userCoupon.getCouponYn()).isEqualTo("N");
//    }
//
//
//
//
//
//}
