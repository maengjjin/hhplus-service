package kr.hhplus.be.server.domain.statistics;

import lombok.Getter;

@Getter
public class ProductOrderVolume {

    private long productId;

    private long orderQty;

    public ProductOrderVolume(long productId, long orderQty) {
        this.productId = productId;
        this.orderQty = orderQty;
    }
}
