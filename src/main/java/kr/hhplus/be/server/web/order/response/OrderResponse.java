package kr.hhplus.be.server.web.order.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponse {

    private long userId;

    private long totalAmount;

    private String orderAt;

    public static OrderResponse of(long userId, long totalAmount,String orderAt) {
        return new OrderResponse(userId, totalAmount, orderAt);
    }

}
