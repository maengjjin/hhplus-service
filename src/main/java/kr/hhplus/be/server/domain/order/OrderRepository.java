package kr.hhplus.be.server.domain.order;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {

    Order saveOrder(Order order);

    void saveOrderDetail(OrderDetail detail);
}
