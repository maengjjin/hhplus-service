package kr.hhplus.be.server.infrastructure.event;

import java.util.List;
import kr.hhplus.be.server.application.event.OrderEventPublisher;
import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankCommand;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent.ProductRankItemEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component("springEventPublisher")
@RequiredArgsConstructor
public class OrderSpringEventPublisher implements OrderEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;


    @Override
    public void sendOrderToExternalPlatform(long orderId, long userId) {

        applicationEventPublisher.publishEvent(new OrderEvent(orderId, userId));
    }

    @Override
    public void publishProductSalesStatistics(ProductRankCommand command) {

        List<ProductRankItemEvent> items = ProductRankEvent.form(command);
        applicationEventPublisher.publishEvent(new ProductRankEvent(items, command.getStatDate()));
    }
}
