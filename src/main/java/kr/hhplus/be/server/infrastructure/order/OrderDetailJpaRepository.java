package kr.hhplus.be.server.infrastructure.order;


import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.statistics.ProductOrderVolume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface OrderDetailJpaRepository extends JpaRepository<OrderDetail, Long> {

    @Query("""
            SELECT new kr.hhplus.be.server.domain.statistics.ProductOrderVolume(o.productId, sum(o.orderQty) as totalQty)
            FROM OrderDetail o
            WHERE o.createAt >= :start
            AND o.createAt < :end
            GROUP BY  o.productId
          """)
    List<ProductOrderVolume> findAggregateTopOrders(LocalDateTime start, LocalDateTime end);
}
