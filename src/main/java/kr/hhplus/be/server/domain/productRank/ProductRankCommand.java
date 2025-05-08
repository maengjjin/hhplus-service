package kr.hhplus.be.server.domain.productRank;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.order.OrderStats;
import lombok.Getter;

@Getter
public class ProductRankCommand {

    private final long productId;

    private final long orderQty;

    private final long orderRank;

    private final LocalDate statDate;


    public ProductRankCommand(long productId, long orderQty, long orderRank, LocalDate statDate) {
        this.productId = productId;
        this.orderQty = orderQty;
        this.orderRank = orderRank;
        this.statDate = statDate;
    }

    public static List<ProductRankCommand> toCommand(List<OrderStats> orderStats, LocalDate statDate) {
        return orderStats.stream()
            .map(order -> new ProductRankCommand(order.getProductId(), order.getOrderQty(), order.getOrderRank(),statDate))
            .collect(Collectors.toList());
    }

}
