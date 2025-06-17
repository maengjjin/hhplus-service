package kr.hhplus.be.server.interfaces.event;

import kr.hhplus.be.server.domain.external.ExternalPlatformService;
import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaOrderConsumer {

    private final ExternalPlatformService externalPlatformService;

    private final ProductRankService productRankService;


    @KafkaListener(topics = "order-platform", containerFactory = "kafkaListenerContainerFactory", groupId = "hhplus-consumer-group")
    public void handleOrderCreatedEvent(@Payload OrderEvent orderEvent, Acknowledgment ack) {
        externalPlatformService.sendOrderInfoToExternalPlatform(orderEvent);
        ack.acknowledge();


    }

    @KafkaListener(topics = "order-product-rank", containerFactory = "kafkaListenerContainerFactory", groupId = "hhplus-consumer-group")
    public void handleOrderCreatedEvent(@Payload ProductRankEvent productRankEvent, Acknowledgment ack) {
        productRankService.incrementDailyProductSales(productRankEvent);
        ack.acknowledge();

    }

}
