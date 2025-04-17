package kr.hhplus.be.server.domain.order;


import java.util.List;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    OrderRepository orderRepository;

    public Order createOrder(List<OrderCommand.OrderItemDetail> item, long userId) {

        // 주문 저장
        Order order = orderRepository.saveOrder(userId, Order.createOrderNumber(), OrderStatus.ORDERED);

        // 주문 옵션
        orderRepository.saveOrderDetail(order.getOrderId(), item);

        return order;

    }

}
