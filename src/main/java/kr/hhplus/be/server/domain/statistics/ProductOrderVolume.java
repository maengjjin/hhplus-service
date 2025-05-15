package kr.hhplus.be.server.domain.statistics;

import lombok.Getter;

@Getter
public class ProductOrderVolume {

    private long productId;

    private long totalQty;

    public ProductOrderVolume(long productId, long totalQty) {
        this.productId = productId;
        this.totalQty = totalQty;
    }
}
