package kr.hhplus.be.server.domain.order;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;


@Component
public interface OrderRepository {

    Order saveOrder(Order order);

    void saveOrderDetail(OrderDetail detail);

    Optional<Order> findByOrderNoAndUserId(String orderNo, long userId);
}
