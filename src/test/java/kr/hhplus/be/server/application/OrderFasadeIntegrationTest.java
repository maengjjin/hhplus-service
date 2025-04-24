
package kr.hhplus.be.server.application;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kr.hhplus.be.server.Exception.CouponException.CouponMinimumAmountNotMetException;
import kr.hhplus.be.server.Exception.PointException.InsufficientPointBalanceException;
import kr.hhplus.be.server.Exception.ProductException.OutOfStockException;
import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResponse;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderPaymentCalculator;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductOption;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.infrastructure.product.ProductOptionJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@Testcontainers
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderFasadeIntegrationTest extends DatabaseConnectionTest {


    @Autowired
    OrderFacade orderFacade;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    ProductOptionJpaRepository productOptionJpaRepository;


    @Autowired
    EntityManager em;



    @Test
    @Transactional
    void 주문_결제_성공_통합테스트() {
        // given 사용자, 상품, 옵션, 쿠폰, 유저쿠폰 세팅
        User user = userRepository.save(User.builder().point(100000L).build());
        Product product = productRepository.save(new Product("후드티", 5000L, ProductStatus.ACTIVE));

        List<ProductOption> options = List.of(
            new ProductOption("검정색", 5000L, 10L, product),
            new ProductOption("흰색", 5000L, 5L, product),
            new ProductOption("회색", 5000L, 7L, product)
        );
        for (ProductOption opt : options) {
            productRepository.save(opt);
        }

        Coupon coupon = couponRepository.save(new Coupon("1000원 할인 쿠폰", CouponType.FIXED, 0, 3000, 1000, 3000, 100, 100, LocalDateTime.now().plusDays(30)));
        UserCoupon userCoupon = couponRepository.save(new UserCoupon(user, coupon, "N"));

        OrderCriteria criteria = new OrderCriteria(
            user.getUserId(),
            coupon.getCouponId(),
            List.of(
                new OrderCriteria.OrderItem(product.getProductId(), options.get(0).getOptionId(), 2L),
                new OrderCriteria.OrderItem(product.getProductId(), options.get(1).getOptionId(), 2L),
                new OrderCriteria.OrderItem(product.getProductId(), options.get(2).getOptionId(), 2L)
            )
        );

        // when 주문 생성
        OrderResponse result = orderFacade.createOrderWithPayment(criteria);

        em.flush();
        em.clear();


        // then 주문, 결제, 포인트, 쿠폰, 재고 검증
        Optional<Order> order = orderRepository.findByOrderNoAndUserId(result.getOrderNo(), user.getUserId());
        assertThat(order).isPresent();
        assertThat(order.get().getOrderNo()).isEqualTo(result.getOrderNo());

        Optional<Payment> payment = paymentRepository.findByPaymentIdAndUserId(result.getPaymentId(), user.getUserId());
        assertThat(payment).isPresent();
        assertThat(payment.get().getPaidPay()).isEqualTo(27000L);

        User updatedUser = userRepository.findById(user.getUserId()).orElseThrow();
        assertThat(updatedUser.getPoint()).isEqualTo(100000L - 27000L);

        UserCoupon usedCoupon = couponRepository.findUserCouponsWithInfo(user.getUserId(), coupon.getCouponId());
        assertThat(usedCoupon.getCouponYn()).isEqualTo("Y");

        List<OrderCriteria.OrderItem> orderItems = criteria.getItems();
        for (ProductOption opt : options) {
            long optionId = opt.getOptionId();
            long orderedQty = orderItems.stream()
                .filter(i -> i.getOptionId() == optionId)
                .mapToLong(OrderCriteria.OrderItem::getQty)
                .sum();

            Optional<ProductOption> updatedOpt = productOptionJpaRepository.findOptionWithProduct(opt.getProduct().getProductId(), optionId);
            long expectedStock = opt.getStockQty() - orderedQty;
            assertThat(updatedOpt.get().getStockQty()).isEqualTo(expectedStock);
        }
    }

    @Test
    void 쿠폰_최소금액_미달이면_예외_발생() {
        // given 총 주문금액이 쿠폰 최소 사용 조건에 미달함
        User user = userRepository.save(User.builder().point(10000L).build());
        Product product = productRepository.save(new Product("후드티", 1000L, ProductStatus.ACTIVE));
        ProductOption option = productRepository.save(new ProductOption("검정색", 1000L, 10L, product));
        Coupon coupon = couponRepository.save(new Coupon("3000원 이상 사용 가능 쿠폰", CouponType.FIXED, 0, 3000, 3000, 3000, 100, 100, LocalDateTime.now().plusDays(30)));
        UserCoupon userCoupon = couponRepository.save(new UserCoupon(user, coupon, "N"));

        OrderCriteria criteria = new OrderCriteria(
            user.getUserId(),
            coupon.getCouponId(),
            List.of(new OrderCriteria.OrderItem(product.getProductId(), option.getOptionId(), 2L)) // 총액: 2000
        );

        // when & then 예외 발생 검증
        assertThatThrownBy(() -> orderFacade.createOrderWithPayment(criteria))
            .isInstanceOf(CouponMinimumAmountNotMetException.class);
    }

    @Test
    void 포인트_부족하면_예외_발생() {
        // given 사용자 포인트가 결제금액보다 적음
        User user = userRepository.save(User.builder().point(1000L).build());
        Product product = productRepository.save(new Product("후드티", 5000L, ProductStatus.ACTIVE));
        ProductOption option = productRepository.save(new ProductOption("검정색", 5000L, 10L, product));
        Coupon coupon = couponRepository.save(new Coupon("1000원 할인 쿠폰", CouponType.FIXED, 0, 3000, 1000, 3000, 100, 100, LocalDateTime.now().plusDays(30)));
        UserCoupon userCoupon = couponRepository.save(new UserCoupon(user, coupon, "N"));

        OrderCriteria criteria = new OrderCriteria(
            user.getUserId(),
            coupon.getCouponId(),
            List.of(new OrderCriteria.OrderItem(product.getProductId(), option.getOptionId(), 3L)) // 총액 15000 - 1000 = 14000
        );

        // when & then 예외 발생 검증
        assertThatThrownBy(() -> orderFacade.createOrderWithPayment(criteria))
            .isInstanceOf(InsufficientPointBalanceException.class);
    }

    @Test
    void 재고_부족하면_예외_발생() {
        // given 상품 옵션 재고가 주문 수량보다 적음
        User user = userRepository.save(User.builder().point(20000L).build());
        Product product = productRepository.save(new Product("후드티", 5000L, ProductStatus.ACTIVE));
        ProductOption option = productRepository.save(new ProductOption("검정색", 5000L, 2L, product)); // 재고 2
        Coupon coupon = couponRepository.save(new Coupon("1000원 할인 쿠폰", CouponType.FIXED, 0, 3000, 10000, 3000, 100, 100, LocalDateTime.now().plusDays(30)));
        UserCoupon userCoupon = couponRepository.save(new UserCoupon(user, coupon, "N"));

        OrderCriteria criteria = new OrderCriteria(
            user.getUserId(),
            coupon.getCouponId(),
            List.of(new OrderCriteria.OrderItem(product.getProductId(), option.getOptionId(), 3L)) // 수량 3
        );

        // when & then 예외 발생 검증
        assertThatThrownBy(() -> orderFacade.createOrderWithPayment(criteria))
            .isInstanceOf(OutOfStockException.class);
    }

    @Test
    void 쿠폰_예외_발생시_트랜잭션_롤백_검증() {
        // given 쿠폰 최소 금액 조건을 충족하지 못함
        User user = userRepository.save(User.builder().point(10000L).build());
        Product product = productRepository.save(new Product("후드티", 1000L, ProductStatus.ACTIVE));
        ProductOption option = productRepository.save(new ProductOption("검정색", 1000L, 10L, product));
        Coupon coupon = couponRepository.save(new Coupon("3000원 이상 사용 가능 쿠폰", CouponType.FIXED, 0, 3000, 3000, 3000, 100, 100, LocalDateTime.now().plusDays(30)));
        UserCoupon userCoupon = couponRepository.save(new UserCoupon(user, coupon, "N"));

        OrderCriteria criteria = new OrderCriteria(
            user.getUserId(),
            coupon.getCouponId(),
            List.of(new OrderCriteria.OrderItem(product.getProductId(), option.getOptionId(), 1L)) // 총액: 1000
        );

        // when 예외 발생 시도
        try {
            orderFacade.createOrderWithPayment(criteria);
        } catch (CouponMinimumAmountNotMetException e) {
            // then 롤백 검증
            Optional<Order> order = orderRepository.findByOrderNoAndUserId("임의값 or 생성된 값", user.getUserId());
            assertThat(order).isEmpty();

            Optional<Payment> payment = paymentRepository.findByPaymentIdAndUserId(0L, user.getUserId());
            assertThat(payment).isEmpty();
        }
    }

}
