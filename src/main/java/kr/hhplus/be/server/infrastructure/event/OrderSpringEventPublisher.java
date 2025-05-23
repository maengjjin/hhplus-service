package kr.hhplus.be.server.infrastructure.event;

import kr.hhplus.be.server.application.event.OrderEventPublisher;
import kr.hhplus.be.server.domain.order.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderSpringEventPublisher implements OrderEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    public void sendOrderToExternalPlatform(long orderId, long userId) {

        applicationEventPublisher.publishEvent(new OrderEvent(orderId, userId));
    }
}
