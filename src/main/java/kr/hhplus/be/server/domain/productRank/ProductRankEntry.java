package kr.hhplus.be.server.domain.productRank;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ProductRankEntry {

    private List<ProductSales> productSales;
    private LocalDate statDate;

    public ProductRankEntry(List<ProductSales> productSales, LocalDate statDate) {
        this.productSales = productSales;
        this.statDate = statDate;
    }


    public static ProductRankEntry fromCommand(ProductRankCommand command) {
        List<ProductSales> rankItems = command.getOrderStats().stream()
            .map(order -> new ProductSales(order.getProductId(), order.getTotalQty()))
            .collect(Collectors.toList());

        return new ProductRankEntry(rankItems, command.getStatDate());
    }


    @Getter
    public static class ProductSales {
        private long productId;
        private long totalQty;

        public ProductSales(long productId, long totalQty) {
            this.productId = productId;
            this.totalQty = totalQty;
        }
    }

}
