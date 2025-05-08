package kr.hhplus.be.server.application.productRank;

import java.time.LocalDate;
import java.util.List;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStats;
import kr.hhplus.be.server.domain.productRank.ProductRankCommand;
import kr.hhplus.be.server.domain.productRank.ProductRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductRankFacade {

    private final ProductRankService productRankService;

    private final OrderService  orderService;

    @Transactional
    public void aggregateAndSaveTopRankedProducts(LocalDate targetDate){

        List<OrderStats> orderStats = orderService.findAggregateTopOrders(targetDate);

        productRankService.saveProductSalesRanking(ProductRankCommand.toCommand(orderStats, targetDate));

    }

}