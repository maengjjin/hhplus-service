package kr.hhplus.be.server.application;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.domain.product.ProductDTO.ProductOrderResult;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.productRank.ProductRankCommand;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent;
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
    void 외부_플랫폼_전송_이벤트가_제대로_데이터로_발행됐는지_검증(){

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


    @Test
    void 주문_통계_이벤트가_제대로_데이터로_발행됐는지_검증(){

        // given 도메인이벤트 세팅
        List<ProductOrderResult> orderItems = List.of(
            new ProductOrderResult(1L, 100L, 50L, 2L, ProductStatus.ACTIVE, 15000L, "맥북 프로", "16인치"),
            new ProductOrderResult(2L, 101L, 30L, 1L, ProductStatus.ACTIVE, 20000L, "아이패드 프로", "12.9인치"),
            new ProductOrderResult(3L, 102L, 100L, 5L, ProductStatus.ACTIVE, 1000L, "애플펜슬", "2세대")
        );

        LocalDate date = LocalDate.now();

        // when 이벤트 발행 메서드 호출
        orderSpringEventPublisher.publishProductSalesStatistics(ProductRankCommand.toCommand(orderItems, date));

        // then 이벤트 발행 검증 및 캡처된 이벤트의 데이터 검증
        ArgumentCaptor<ProductRankEvent> eventCaptor = ArgumentCaptor.forClass(ProductRankEvent.class);
        verify(applicationEventPublisher).publishEvent(eventCaptor.capture());

        ProductRankEvent capturedEvent = eventCaptor.getValue();
        assertEquals(date, capturedEvent.getDate());
        assertEquals(orderItems.size(), capturedEvent.getItems().size());


    }

}
