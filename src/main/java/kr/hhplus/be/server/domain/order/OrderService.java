package kr.hhplus.be.server.domain.order;


import java.util.List;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(List<OrderCommand.OrderItem> orderItems, long userId) {

        // 주문 저장
        Order order = orderRepository.saveOrder(new Order(new User(userId), OrderStatus.ORDERED));

        List<OrderDetail> details = orderItems.stream()
            .map(item -> new OrderDetail(order.getOrderId(), item.getProductId(), item.getOptionId(), item.getQty(), item.getPrice()))
            .toList();

        orderRepository.saveAll(details);

        return order;

    }

}
