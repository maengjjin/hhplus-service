package kr.hhplus.be.server.application;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.sql.DataSource;
import kr.hhplus.be.server.Exception.CouponException.CouponOutOfStockException;
import kr.hhplus.be.server.Exception.UserException.UserNotFoundException;
import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CouponFasadeSyncIntegrationTest {

    @Autowired
    CouponFacade couponFacade;



    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserRepository userRepository;



    @Autowired
    DataSource dataSource;

    @Test
    void DB연결테스트() {

        User user = userRepository.findById(500L).orElseThrow(UserNotFoundException::new);
        Coupon coupon = couponRepository.findById(1L).orElseThrow(CouponOutOfStockException::new);
        System.out.println(user.getUserId());
        System.out.println("coupon. = " + coupon.getName());

    }

    @Test
    void printDbConnectionInfo() throws Exception {
        System.out.println(">>> DB URL: " + dataSource.getConnection().getMetaData().getURL());
    }

//    @Test
//    void 동시에_쿠폰_발급_요청하면_수량_초과되지_않는다() throws Exception {
//        // given: 수량 1인 쿠폰 저장
//        Coupon coupon = couponRepository.save(new Coupon(
//            "선착순 쿠폰", CouponType.FIXED,
//            0, 3000, 1000, 3000, 1, 1,
//            LocalDateTime.now().plusDays(1)
//        ));
//
//        User user1 = userRepository.save(User.builder().point(1000L).build());
//        User user2 = userRepository.save(User.builder().point(1000L).build());
//
//        CouponCommand command1 = new CouponCommand(user1.getUserId(), coupon.getCouponId());
//        CouponCommand command2 = new CouponCommand(user2.getUserId(), coupon.getCouponId());
//
//        // when: 동시에 두 명이 발급 시도
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//
//        List<Callable<String>> tasks = List.of(
//            () -> {
//                try {
//                    couponFacade.createCoupon(command1);
//                    return "SUCCESS_1";
//                } catch (CouponOutOfStockException e) {
//                    return "FAIL_1";
//                }
//            },
//            () -> {
//                try {
//                    couponFacade.createCoupon(command2);
//                    return "SUCCESS_2";
//                } catch (CouponOutOfStockException e) {
//                    return "FAIL_2";
//                }
//            }
//        );
//
//        List<Future<String>> futures = executor.invokeAll(tasks);
//        executor.shutdown();
//
//        List<String> results = new ArrayList<>();
//        for (Future<String> future : futures) {
//            results.add(future.get());
//        }
//
//        // then: 성공은 1명, 실패는 1명이어야 함
//        long successCount = results.stream().filter(r -> r.startsWith("SUCCESS")).count();
//        long failCount = results.stream().filter(r -> r.startsWith("FAIL")).count();
//
//        assertThat(successCount).isEqualTo(1);
//        assertThat(failCount).isEqualTo(1);
//
//        // 실제 쿠폰 수량 확인
//        Coupon updated = couponRepository.findById(coupon.getCouponId()).orElseThrow();
//        assertThat(updated.getLeftQty()).isEqualTo(0L);
//    }
//
//    @Test
//    void 동시에_쿠폰_발급_요청하면_수량이_잘못_된다() throws Exception {
//
//
//        ExecutorService executor = Executors.newFixedThreadPool(7); // 7명 동시 도전
////
//        CountDownLatch startLatch = new CountDownLatch(1); // 동시에 시작 시킬 용도
//
//        // 7개의 커맨드 생성
//        List<CouponCommand> commands = List.of(
//            new CouponCommand(400, 1000),
//            new CouponCommand(401,1000),
//            new CouponCommand(402,1000),
//            new CouponCommand(403,1000),
//            new CouponCommand(404,1000),
//            new CouponCommand(405,1000),
//            new CouponCommand(406, 1000)
//        );
//
//
//        List<Callable<String>> tasks = new ArrayList<>();
//        for (int i = 0; i < commands.size(); i++) {
//            int index = i;
//            tasks.add(() -> {
//                String label = String.valueOf(index + 1);
//                try {
//                    System.out.println("스레드 " + label + ": 대기 중");
//                    startLatch.await();
//                    System.out.println("스레드 " + label + ": 시작!");
//                    couponFacade.createCoupon(commands.get(index));
//                    System.out.println("스레드 " + label + ": SUCCESS");
//                    return "SUCCESS";
//                } catch (CouponOutOfStockException e) {
//                    System.out.println("스레드 " + label + ": FAIL - 재고 없음");
//                    return "FAIL";
//                } catch (Exception e) {
//                    System.out.println("스레드 " + label + ": ERROR - " + e.getMessage());
//                    return "ERROR";
//                }
//            });
//        }
//
//        List<Future<String>> futures = tasks.stream().map(executor::submit).toList();
//
//        System.out.println(">> 모든 스레드 준비 완료. 시작!");
//        startLatch.countDown(); // 동시에 실행
//        executor.shutdown();
//
//        List<String> results = new ArrayList<>();
//        for (Future<String> future : futures) {
//            results.add(future.get());
//        }
//
//        long success = results.stream().filter(r -> r.equals("SUCCESS")).count();
//        long fail = results.stream().filter(r -> r.equals("FAIL")).count();
//        long error = results.stream().filter(r -> r.equals("ERROR")).count();
//
//        System.out.println("성공: " + success + " / 실패: " + fail + " / 오류: " + error);
//    }
//

}
