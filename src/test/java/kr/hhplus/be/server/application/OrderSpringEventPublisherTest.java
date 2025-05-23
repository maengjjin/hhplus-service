package kr.hhplus.be.server.application;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.infrastructure.event.OrderSpringEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
public class OrderSpringEventPublisherTest {

    @Mock
    ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    OrderSpringEventPublisher orderSpringEventPublisher;

    @Test
    void 이벤트가_제대로_데이터로_발행됐는지_검증(){

        // given 도메인이벤트 세팅
        long orderId = 10L;
        long userId = 1L;

        // when 이벤트 발행 메서드 호출
        orderSpringEventPublisher.sendOrderToExternalPlatform(orderId, userId);

        // then 이벤트 발행 검증 및 캡처된 이벤트의 데이터 검증
        ArgumentCaptor<OrderEvent> eventCaptor = ArgumentCaptor.forClass(OrderEvent.class);
        verify(applicationEventPublisher).publishEvent(eventCaptor.capture());

        OrderEvent capturedEvent = eventCaptor.getValue();
        assertEquals(orderId, capturedEvent.getOrderId());
        assertEquals(userId, capturedEvent.getUserId());

    }

}
