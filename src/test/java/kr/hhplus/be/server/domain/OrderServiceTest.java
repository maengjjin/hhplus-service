package kr.hhplus.be.server.domain;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.OrderItemInfo;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.user.User;
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
    void 주문_상세_저장_성공(){

        // given 주문 정보 생성
        long userId = 1L;
        String fakeOrderNo = "20250416173244463";
        Order fakeOrder = new Order(new User(userId), OrderStatus.ORDERED);

        when(orderRepository.saveOrder(any(Order.class))).thenReturn(fakeOrder);

        List<OrderCommand.OrderItemDetail> item = List.of(
            new OrderCommand.OrderItemDetail(1L, 100L, 1, 30000L),
            new OrderCommand.OrderItemDetail(1L, 102L, 1, 30000L),
            new OrderCommand.OrderItemDetail(3L, 101L, 3, 3000L)
        );

        // 정적 메서드 Order.createOrderNumber()가 호출되면 fakeOrderNo 반환
        try (MockedStatic<Order> mockedStatic = Mockito.mockStatic(Order.class)) {
            mockedStatic.when(Order::createOrderNumber).thenReturn(fakeOrderNo);

            // when 주문 생성 요청 실행
            orderService.createOrder(item, userId);

            // then 주문이 한 번 저장되고, 주문 상세가 정확히 3번 저장됐는지 검증
            verify(orderRepository, times(1)).saveOrder(any(Order.class));
            verify(orderRepository, times(3)).saveOrderDetail(any(OrderDetail.class)); // 3개니까
        }


    }


}
