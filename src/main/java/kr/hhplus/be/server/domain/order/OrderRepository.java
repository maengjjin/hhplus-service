package kr.hhplus.be.server.domain.order;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository {


    Order saveOrder(long userId, String orderNo, OrderStatus ordered);

    void saveOrderDetail(long orderId, List<OrderCommand.OrderItemDetail> item);
}
