package kr.hhplus.be.server.domain.order;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;


@Component
public interface OrderRepository {

    Order saveOrder(Order order);

    void saveAll(List<OrderDetail> details);

    Optional<Order> findByOrderNoAndUserId(String orderNo, long userId);
}
