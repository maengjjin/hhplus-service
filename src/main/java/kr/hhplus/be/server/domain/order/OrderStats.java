package kr.hhplus.be.server.domain.order;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class OrderStats {

    private final long productId;

    private final long orderQty;

    private final long orderRank;

    private final LocalDate statDate;

    public OrderStats(long productId, long orderQty, long orderRank, LocalDate statDate) {
        this.productId = productId;
        this.orderQty = orderQty;
        this.orderRank = orderRank;
        this.statDate = statDate;
    }
}
