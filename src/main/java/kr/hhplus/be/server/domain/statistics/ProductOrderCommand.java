package kr.hhplus.be.server.domain.statistics;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.productRank.ProductRankEntry;
import lombok.Getter;

@Getter
public class ProductOrderCommand {
    private final List<OrderStatsData> orderStats;
    private final LocalDate statDate;

    public ProductOrderCommand(List<OrderStatsData> orderStats, LocalDate statDate) {
        this.orderStats = orderStats;
        this.statDate = statDate;
    }

    public static ProductOrderCommand toCommand(List<ProductRankEntry.ProductSales> productSales, LocalDate date) {

        List<OrderStatsData> statsData = productSales.stream()
            .map(sale -> new OrderStatsData(sale.getProductId(), sale.getOrderQty()))
            .collect(Collectors.toList());

        return new ProductOrderCommand(statsData, date);
    }

    @Getter
    public static class OrderStatsData {
        private final long productId;
        private final long orderQty;

        public OrderStatsData(long productId, long orderQty) {
            this.productId = productId;
            this.orderQty = orderQty;
        }
    }

}
