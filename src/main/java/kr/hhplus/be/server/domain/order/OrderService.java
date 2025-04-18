package kr.hhplus.be.server.domain.order;


import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.stereotype.Service;


@Service
public class OrderService {

    OrderRepository orderRepository;

    public Order createOrder(List<OrderCommand.OrderItemDetail> item, long userId) {

        // 주문 저장
        Order order = orderRepository.saveOrder(Order.of(new User(userId), OrderStatus.ORDERED));


        List<OrderDetail> details = item.stream()
            .map(info -> OrderDetail.of(info, order.getOrderId())) // orderId는 이미 생성된 주문 ID
            .collect(Collectors.toList());


        for (OrderDetail detail : details) {
            orderRepository.saveOrderDetail(detail);
        }

        return order;

    }

}
