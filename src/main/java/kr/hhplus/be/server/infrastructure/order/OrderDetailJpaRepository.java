package kr.hhplus.be.server.infrastructure.order;


import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.OrderStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface OrderDetailJpaRepository extends JpaRepository<OrderDetail, Long> {

    @Query("""
            SELECT o.productId, sum(o.orderQty)
            FROM OrderDetail o
            WHERE o.createAt < :end AND o.createAt > :start
            GROUP BY  o.productId
          """)
    List<OrderStats> findTopSellingProductsBetween(LocalDateTime start, LocalDateTime end);
}
