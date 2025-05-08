package kr.hhplus.be.server.domain.order;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
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

    public List<OrderStats> findAggregateTopOrders(LocalDate date){

        LocalDateTime end = date.atStartOfDay();
        LocalDateTime start =  end.minusDays(3);


        // 인기순위 5개 정렬
        return orderRepository.findTopSellingProductsBetween(start, end).stream()
            .sorted(Comparator.comparing(OrderStats::getOrderQty).reversed()) // 주문 수량 기준 내림차순
            .limit(5)
            .toList();

    }

}
