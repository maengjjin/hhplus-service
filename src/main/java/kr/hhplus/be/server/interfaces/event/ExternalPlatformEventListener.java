package kr.hhplus.be.server.interfaces.event;

import kr.hhplus.be.server.domain.external.ExternalPlatformService;
import kr.hhplus.be.server.domain.order.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ExternalPlatformEventListener {

    private final ExternalPlatformService externalPlatformService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreatedEvent(OrderEvent event) {

        externalPlatformService.sendOrderInfoToExternalPlatform(event);
    }


}
