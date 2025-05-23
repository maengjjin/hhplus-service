package kr.hhplus.be.server.domain.productRank;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ProductRankEvent {

    private List<ProductRankItemEvent> items;

    private LocalDate date;

    public ProductRankEvent(List<ProductRankItemEvent> items, LocalDate date) {
        this.items = items;
        this.date = date;
    }

    public static List<ProductRankItemEvent> form(ProductRankCommand command){
        return command.getOrderStats().stream()
            .map(event -> new ProductRankItemEvent(event.getProductId(), event.getOrderQty()))
            .collect(Collectors.toList());
    }


    @Getter
    public static class ProductRankItemEvent{

        private long productId;

        private long orderQty;

        public ProductRankItemEvent(long productId, long orderQty) {
            this.productId = productId;
            this.orderQty = orderQty;
        }
    }
}
