//package kr.hhplus.be.server.infrastructure.order;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//import jakarta.persistence.EntityManager;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import kr.hhplus.be.server.domain.order.OrderDetail;
//import kr.hhplus.be.server.domain.statistics.ProductOrderVolume;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.transaction.annotation.Transactional;
//
//@Transactional
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//public class OrderJpaRepositoryTest {
//
//
//    @Autowired
//    OrderDetailJpaRepository orderDetailJpaRepository;
//
//    @Autowired
//    EntityManager em;
//
//    @Test
//    void 상품과_옵션_연관_조회_정상_수행() {
//        // given 상품과 옵션을 저장하고 양방향 연관관계 연결
//        OrderDetail orderDetail = new OrderDetail(1, 1, 1, 2000, 10);
//
//        OrderDetail orderDetail2 = new OrderDetail(1, 1, 1, 2000, 10);
//
//        OrderDetail orderDetail3 = new OrderDetail(1, 4, 1, 2000, 15);
//
//        List<OrderDetail> orderDetailList = List.of(orderDetail, orderDetail2, orderDetail3);
//        orderDetailJpaRepository.saveAll(orderDetailList);
//
//        em.flush(); // DB 반영
//        em.clear(); // 영속성 컨텍스트 초기화 → 진짜 DB에서 조회됨
//
//        LocalDate date = LocalDate.now();
//        LocalDateTime start = date.atStartOfDay();
//        LocalDateTime end = date.plusDays(1).atStartOfDay();
//
//        List<ProductOrderVolume> result = orderDetailJpaRepository.findAggregateTopOrders(start, end);
//
//        Assertions.assertThat(result).hasSize(2);
//        assertThat(result.get(0).getOrderQty()).isEqualTo(20);
//        assertThat(result.get(1).getOrderQty()).isEqualTo(15); // 예시 수량
//
//
//
//
//    }
//
//}
