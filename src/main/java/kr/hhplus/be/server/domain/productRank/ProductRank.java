package kr.hhplus.be.server.domain.productRank;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ProductRank {

    private long productId;

    private String productName;

    private long rank;

    private LocalDate statDate;

    public ProductRank(long productId, String productName, long rank, LocalDate statDate) {
        this.productId = productId;
        this.productName = productName;
        this.rank = rank;
        this.statDate = statDate;
    }

}
