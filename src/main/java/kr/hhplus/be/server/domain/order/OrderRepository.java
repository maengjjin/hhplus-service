package kr.hhplus.be.server.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.productRank.ProductRank;
import org.springframework.stereotype.Component;


@Component
public interface OrderRepository {

    Order saveOrder(Order order);

    void saveAll(List<OrderDetail> details);

    List<OrderStats> findTopSellingProductsBetween(LocalDateTime start, LocalDateTime end);
}
