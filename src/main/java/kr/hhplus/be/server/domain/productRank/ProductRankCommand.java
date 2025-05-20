package kr.hhplus.be.server.domain.productRank;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.product.ProductDTO.ProductOrderResult;
import lombok.Getter;

@Getter
public class ProductRankCommand {


    private  List<ProductRankCommand.OrderStats> orderStats;

    private  LocalDate statDate;


    public ProductRankCommand(List<OrderStats> orderStats, LocalDate statDate) {
        this.orderStats = orderStats;
        this.statDate = statDate;
    }


    public static ProductRankCommand toCommand(List<ProductOrderResult> orderVolume, LocalDate statDate) {
        List<ProductRankCommand.OrderStats> orderStats = orderVolume.stream()
            .map(order -> new ProductRankCommand.OrderStats(order.getProductId(), order.getOrderQty()))
            .collect(Collectors.toList());

        return new ProductRankCommand(orderStats, statDate);
    }


    @Getter
    public static class OrderStats {

        private  long productId;

        private  long orderQty;

        public OrderStats(long productId, long orderQty) {
            this.productId = productId;
            this.orderQty = orderQty;
        }
    }

}
