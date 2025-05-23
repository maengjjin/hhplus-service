package kr.hhplus.be.server.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.order.OrderCriteria.OrderItem;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.external.ExternalPlatformService;
import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductOption;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;


@RecordApplicationEvents
@SpringBootTest
public class OrderEventAsyncIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    ProductRepository productRepository;

    @MockitoSpyBean
    ProductRankService productRankService;

    @MockitoSpyBean
    private ExternalPlatformService externalPlatformService;
    
    @Autowired
    private ApplicationEvents applicationEvents;


    @BeforeEach
    void setup() {
        // 사용자 데이터 저장
        User savedUser = userRepository.save(User.withPoint(300000L));

        // 2. 저장된 사용자의 ID 사용
        long userId = savedUser.getUserId();



        Coupon coupon1 = couponRepository.save(new Coupon(
            "5천원 할인 쿠폰",
            CouponType.FIXED,
            0,
            5000,
            10000,
            5000,
            1000,
            1,
            LocalDateTime.of(2025, 5, 30, 23, 59) // expiresAt
        ));

        UserCoupon userCoupon = couponRepository.save(UserCoupon.create(userId, coupon1)); // userId 1L에게 쿠폰 할당

        // 상품 데이터 저장

        Coupon coupon2 = couponRepository.save(new Coupon(
            "5천원 할인 쿠폰",
            CouponType.FIXED,
            0,
            5000,
            10000,
            5000,
            1000,
            1,
            LocalDateTime.of(2025, 5, 30, 23, 59) // expiresAt
        ));

        couponRepository.save(UserCoupon.create(userId, coupon2)); // userId 1L에게 쿠폰 할당

        Coupon coupon3 = couponRepository.save(new Coupon(
            "5천원 할인 쿠폰",
            CouponType.FIXED,
            0,
            5000,
            10000,
            5000,
            1000,
            1,
            LocalDateTime.of(2025, 5, 30, 23, 59) // expiresAt
        ));

        couponRepository.save(UserCoupon.create(userId, coupon3)); // userId 1L에게 쿠폰 할당

        Coupon coupon4 = couponRepository.save(new Coupon(
            "5천원 할인 쿠폰",
            CouponType.FIXED,
            0,
            5000,
            10000,
            5000,
            1000,
            1,
            LocalDateTime.of(2025, 5, 30, 23, 59) // expiresAt
        ));

        couponRepository.save(UserCoupon.create(userId, coupon4)); // userId 1L에게 쿠폰 할당


        // 상품 데이터 저장
        Product product1 = productRepository.save(new Product("상품1", 5000, ProductStatus.ACTIVE));

        // 상품 옵션 저장 - product1에 연결
        ProductOption option1 = new ProductOption("옵션1", 5000, 10, product1);
        ProductOption option2 = new ProductOption("옵션2", 6000, 10, product1);
        ProductOption option3 = new ProductOption("옵션3", 7000, 10, product1);

        productRepository.save(option1);
        productRepository.save(option2);
        productRepository.save(option3);



    }


    @Test
    void 주문_완료시_외부플랫폼_전송을_위한_이벤트가_발행되는지_검증() {

        // given: 주문 생성에 필요한 데이터 세팅
        long userId = 1L;
        long couponId = 1L;
        List<OrderItem> items = List.of(
            new OrderItem(1L, 1L, 2L),
            new OrderItem(1L, 2L, 2L),
            new OrderItem(1L, 3L, 2L)
        );
        OrderCriteria orderCriteria = new OrderCriteria(userId, couponId, items);

        // when: 주문 생성 및 이벤트 발행 실행
        orderFacade.createOrder(orderCriteria);

        // then: 이벤트 발행 여부 및 데이터 검증
        assertThat(applicationEvents.stream(OrderEvent.class))
            .hasSize(1)
            .anySatisfy(event -> {
                assertAll(
                    () -> assertThat(event.getUserId()).isEqualTo(userId)
                );
            });

        OrderEvent event = applicationEvents.stream(OrderEvent.class).findFirst().orElseThrow();
        assertThat(event.getUserId()).isEqualTo(userId);
    }

    @Test
    void 주문_생성_트랜잭션_커밋_후_외부플랫폼_이벤트가_비동기적으로_처리되는지_검증() {

        // given: 주문 생성에 필요한 데이터 세팅
        long userId = 1L;
        long couponId = 2L;
        List<OrderItem> items = List.of(
            new OrderItem(1L, 1L, 2L),
            new OrderItem(1L, 2L, 2L),
            new OrderItem(1L, 3L, 2L)
        );
        OrderCriteria orderCriteria = new OrderCriteria(userId, couponId, items);

        // when: 주문 생성 실행
        orderFacade.createOrder(orderCriteria);

        // then: 이벤트 발행 및 비동기 리스너 실행 검증
        assertThat(applicationEvents.stream(OrderEvent.class)).hasSize(1);

        OrderEvent event = applicationEvents.stream(OrderEvent.class).findFirst().orElseThrow();
        assertThat(event.getUserId()).isEqualTo(userId);

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(externalPlatformService).sendOrderInfoToExternalPlatform(any(OrderEvent.class));
        });
    }


    @Test
    void 주문_완료시_상품_통계_이벤트가_발행되는지_검증() {

        // given: 주문 생성에 필요한 데이터 세팅
        long userId = 1L;
        long couponId = 3L;
        List<OrderItem> items = List.of(
            new OrderItem(1L, 1L, 2L),
            new OrderItem(1L, 2L, 2L),
            new OrderItem(1L, 3L, 2L)
        );
        OrderCriteria orderCriteria = new OrderCriteria(userId, couponId, items);

        // when: 주문 생성 및 이벤트 발행 실행
        orderFacade.createOrder(orderCriteria);

        // then: 이벤트 발행 여부 및 데이터 검증
        assertThat(applicationEvents.stream(ProductRankEvent.class))
            .hasSize(1)
            .anySatisfy(event -> {
                assertAll(
                    () -> assertThat(event.getDate()).isEqualTo(LocalDate.now()),
                    () -> assertThat(event.getItems()).hasSize(items.size())
                );
            });
    }

    @Test
    void 주문_생성_트랜잭션_커밋_후_상품_통계_이벤트가_비동기적으로_처리되는지_검증() {

        // given: 주문 생성에 필요한 데이터 세팅
        long userId = 1L;
        long couponId = 4L;
        List<OrderItem> items = List.of(
            new OrderItem(1L, 1L, 2L),
            new OrderItem(1L, 2L, 2L),
            new OrderItem(1L, 3L, 2L)
        );
        OrderCriteria orderCriteria = new OrderCriteria(userId, couponId, items);

        // when: 주문 생성 실행
        orderFacade.createOrder(orderCriteria);

        // then: 이벤트 발행 및 비동기 리스너 실행 검증
        assertThat(applicationEvents.stream(ProductRankEvent.class)).hasSize(1);

        await().atMost(2, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(productRankService).incrementDailyProductSales(any(ProductRankEvent.class));
        });
    }


}
