package kr.hhplus.be.server.interfaces.event;

import kr.hhplus.be.server.domain.productRank.ProductRankEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProductSalesStatisticsListener {


    private final ProductRankService productRankService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCreatedEvent(ProductRankEvent event) {

        productRankService.incrementDailyProductSales(event);
    }

}
