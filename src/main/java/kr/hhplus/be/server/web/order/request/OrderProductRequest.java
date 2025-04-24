package kr.hhplus.be.server.web.order.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderProductRequest {

    private long productId;

    private long optionId;

    private long productPrice;

    private long orderQty;

    public static OrderProductRequest of(long productId, long optionId,long productPrice, long orderQty) {
        return new OrderProductRequest(productId, optionId, productPrice, orderQty);
    }

}
