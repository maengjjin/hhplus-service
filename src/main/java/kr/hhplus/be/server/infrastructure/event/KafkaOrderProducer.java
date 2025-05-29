package kr.hhplus.be.server.infrastructure.event;

import static kr.hhplus.be.server.common.TopicFactory.getOrderPlatformTopic;
import static kr.hhplus.be.server.common.TopicFactory.getTopProductRankKey;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import kr.hhplus.be.server.application.event.OrderEventPublisher;
import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankCommand;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent.ProductRankItemEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component("kafkaOrderProducer")
@RequiredArgsConstructor
public class KafkaOrderProducer implements OrderEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendOrderToExternalPlatform(long orderId, long userId) {
        OrderEvent event = new OrderEvent(orderId, userId);
        String topic = getOrderPlatformTopic();

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(event);
            String key = Long.toString(orderId);

            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, jsonInString);
            future
                .whenComplete((result, ex) -> {
                    if(ex != null){
                        log.error("데이터 플랫폼 메시지 전송 실패 - 토픽: {}, 오류 메시지: {}", topic, ex.getMessage(), ex);
                    } else {
                        log.info("데이터 플랫폼 메시지 전송 성공 - 토픽: {}, 파티션: {}, 오프셋: {}", topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());

                    }
                });
        } catch (Exception ex) {
            log.error("데이터 플랫폼 메시지 전송 오류 - 토픽: {}, 에러 메시지: {}", topic, ex.getMessage(), ex);
        }

    }

    @Override
    public void publishProductSalesStatistics(ProductRankCommand command) {

        List<ProductRankItemEvent> items = ProductRankEvent.form(command);
        String topic = getTopProductRankKey();

        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(items);
            String key = command.getStatDate().format(DateTimeFormatter.BASIC_ISO_DATE);;

            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, jsonInString);
            future
                .whenComplete((result, ex) -> {
                    if(ex != null){
                        log.error("주문 집계 메시지 전송 실패 - 토픽: {}, 오류 메시지: {}", topic, ex.getMessage(), ex);
                    } else {
                        log.info("주문 집계 메시지 전송 성공 - 토픽: {}, 파티션: {}, 오프셋: {}", topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());

                    }
                });
        } catch (Exception ex) {
            log.error("주문 집계 메시지 전송 오류 - 토픽: {}, 에러 메시지: {}", topic, ex.getMessage(), ex);
        }

    }

}
