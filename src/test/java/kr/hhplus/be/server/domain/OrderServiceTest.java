package kr.hhplus.be.server.domain;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderItemInfo;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    // 주문 완료, 주문 완료 실패,

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderService orderService;
//

    long userId;


    @BeforeEach
    void beforeEach(){
        userId = 1L;
    }



    @Test
    void 주문서_저장_성공(){

        String fakeOrderNo = "20250416173244463";
        Order order = new Order(1L, fakeOrderNo, 1L, OrderStatus.ORDERED);


        List<OrderCommand.OrderItemDetail> item = List.of(
            new OrderCommand.OrderItemDetail(1L, 100L, 1, 30000L),
            new OrderCommand.OrderItemDetail(1L, 102L, 1, 30000L),
            new OrderCommand.OrderItemDetail(3L, 101L, 3, 3000L)
        );


        try (MockedStatic<Order> mockedStatic = Mockito.mockStatic(Order.class)) {
            mockedStatic.when(Order::createOrderNumber).thenReturn(fakeOrderNo);

            when(orderRepository.saveOrder(userId, fakeOrderNo, OrderStatus.ORDERED)).thenReturn(order);

            // when
            orderService.createOrder(item, userId);

            // then
            verify(orderRepository, times(1)).saveOrder(userId, fakeOrderNo, OrderStatus.ORDERED);
            verify(orderRepository, times(1)).saveOrderDetail(1L, item);
        }


    }


}
