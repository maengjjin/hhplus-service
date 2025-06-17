package kr.hhplus.be.server.infrastructure.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import kr.hhplus.be.server.common.TopicFactory;
import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankCommand;
import kr.hhplus.be.server.infrastructure.event.KafkaOrderProducer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;


@ExtendWith(MockitoExtension.class)
public class KafkaOrderProducerTest{

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private CompletableFuture<SendResult<String, Object>> future;

    @InjectMocks
    private KafkaOrderProducer kafkaOrderProducer;

    @BeforeEach
    void beforeEach() {
        when(kafkaTemplate.send(any(), any(), any())).thenReturn(future);

        // 메시지 발행 후 결과 콜백을 모킹
        when(future.whenComplete(any())).thenReturn(future);
    }



    @SneakyThrows
    @Test
    void 데이터_플랫폼_정상적으로_메시지_발행_및_발행_내용_검증() {

        // given: 도메인이벤트 세팅
        long orderId = 10L;
        long userId = 1L;
        String topic = TopicFactory.getOrderPlatformTopic();
        String key = String.valueOf(orderId);

        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        // when: 메시지 발행
        kafkaOrderProducer.sendOrderToExternalPlatform(orderId, userId);

        // then: 메시지 발행 내용 검증
        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), messageCaptor.capture());

        assertEquals(topic, topicCaptor.getValue());
        assertEquals(key, keyCaptor.getValue());


        String jsonMessage = messageCaptor.getValue();
        OrderEvent capturedEvent = new ObjectMapper().readValue(jsonMessage, OrderEvent.class);
        assertEquals(orderId, capturedEvent.getOrderId());
        assertEquals(userId, capturedEvent.getUserId());
    }

    @Test
    void 상품_판매_통계_정상적으로_메시지_발행_및_콜백_검증() {

        // given: 도메인이벤트 세팅
        ProductRankCommand command = new ProductRankCommand(
            List.of(
                new ProductRankCommand.OrderStats(1L, 10),
                new ProductRankCommand.OrderStats(2L, 15),
                new ProductRankCommand.OrderStats(3L, 8)
            ),
            LocalDate.now()
        );
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);


        // when: 메시지 발행
        kafkaOrderProducer.publishProductSalesStatistics(command);

        // then: 메시지 발행 콜백 검증
        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), messageCaptor.capture());
        verify(future).whenComplete(any());
    }




}
