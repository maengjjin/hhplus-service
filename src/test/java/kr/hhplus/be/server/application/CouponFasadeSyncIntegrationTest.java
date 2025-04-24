package kr.hhplus.be.server.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import kr.hhplus.be.server.Exception.CouponException.CouponOutOfStockException;
import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponType;
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
public class CouponFasadeSyncIntegrationTest {

    @Autowired
    CouponFacade couponFacade;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void 동시에_쿠폰_발급_요청하면_수량_초과되지_않는다() throws Exception {
        // given: 수량 1인 쿠폰 저장
        Coupon coupon = couponRepository.save(new Coupon(
            "선착순 쿠폰", CouponType.FIXED,
            0, 3000, 1000, 3000, 1, 1,
            LocalDateTime.now().plusDays(1)
        ));

        User user1 = userRepository.save(User.builder().point(1000L).build());
        User user2 = userRepository.save(User.builder().point(1000L).build());

        CouponCommand command1 = new CouponCommand(user1.getUserId(), coupon.getCouponId());
        CouponCommand command2 = new CouponCommand(user2.getUserId(), coupon.getCouponId());

        // when: 동시에 두 명이 발급 시도
        ExecutorService executor = Executors.newFixedThreadPool(2);

        List<Callable<String>> tasks = List.of(
            () -> {
                try {
                    couponFacade.createCoupon(command1);
                    return "SUCCESS_1";
                } catch (CouponOutOfStockException e) {
                    return "FAIL_1";
                }
            },
            () -> {
                try {
                    couponFacade.createCoupon(command2);
                    return "SUCCESS_2";
                } catch (CouponOutOfStockException e) {
                    return "FAIL_2";
                }
            }
        );

        List<Future<String>> futures = executor.invokeAll(tasks);
        executor.shutdown();

        List<String> results = new ArrayList<>();
        for (Future<String> future : futures) {
            results.add(future.get());
        }

        // then: 성공은 1명, 실패는 1명이어야 함
        long successCount = results.stream().filter(r -> r.startsWith("SUCCESS")).count();
        long failCount = results.stream().filter(r -> r.startsWith("FAIL")).count();

        assertThat(successCount).isEqualTo(1);
        assertThat(failCount).isEqualTo(1);

        // 실제 쿠폰 수량 확인
        Coupon updated = couponRepository.findById(coupon.getCouponId()).orElseThrow();
        assertThat(updated.getLeftQty()).isEqualTo(0L);
    }



}
