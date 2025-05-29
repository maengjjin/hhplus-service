package kr.hhplus.be.server.interfaces;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;


import java.time.LocalDate;
import java.util.List;
import kr.hhplus.be.server.domain.external.ExternalPlatformService;
import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent.ProductRankItemEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankService;
import kr.hhplus.be.server.interfaces.event.KafkaOrderConsumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

@ExtendWith(MockitoExtension.class)
public class KafkaOrderConsumerTest {

    @Mock
    ExternalPlatformService externalPlatformService;

    @InjectMocks
    KafkaOrderConsumer orderConsumer;

    @Mock
    ProductRankService productRankService;



    @Test
    void 리스너_메서드가_서비스를_올바르게_호출하는지_검증() {

        // given: 주문 이벤트 세팅
        OrderEvent orderEvent = new OrderEvent(10L, 1L);

        Acknowledgment mockAck = mock(Acknowledgment.class);

        // when: 비동기 이벤트 리스너 실행
        orderConsumer.handleOrderCreatedEvent(orderEvent, mockAck);


        // then: 비동기 작업 실행 검증
        verify(externalPlatformService).sendOrderInfoToExternalPlatform(
            argThat(event ->
                event.getOrderId() == orderEvent.getOrderId() && event.getUserId() == orderEvent.getUserId()
            )
        );

    }


    @Test
    void 상품_랭킹_메시지_수신_후_커밋_검증() {

        // given: 상품 랭킹 데이터 새팅
        ProductRankEvent productRankEvent = new ProductRankEvent(
            List.of(
                new ProductRankItemEvent(1L, 10),
                new ProductRankItemEvent(2L, 15),
                new ProductRankItemEvent(3L, 8)
            ),
            LocalDate.now()
        );

        Acknowledgment mockAck = mock(Acknowledgment.class);

        // when: 리스너 메서드 호출
        orderConsumer.handleOrderCreatedEvent(productRankEvent, mockAck);

        // then: 서비스 호출 및 ack 호출 검증
        verify(mockAck).acknowledge();
        verify(productRankService).incrementDailyProductSales(productRankEvent);
        verifyNoMoreInteractions(mockAck, productRankService);
    }

}