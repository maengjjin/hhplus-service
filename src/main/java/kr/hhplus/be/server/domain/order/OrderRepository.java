package kr.hhplus.be.server.domain.order;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.statistics.ProductOrderVolume;
import org.springframework.stereotype.Component;


@Component
public interface OrderRepository {

    Order saveOrder(Order order);

    void saveAll(List<OrderDetail> details);

    List<ProductOrderVolume> findAggregateTopOrders(LocalDateTime start, LocalDateTime end);

}
