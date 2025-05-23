package kr.hhplus.be.server.domain.order;


import lombok.Getter;

@Getter
public class OrderEvent {

    private long orderId;

    private long userId;

    public OrderEvent(long orderId, long userId) {
        this.orderId = orderId;
        this.userId = userId;
    }
}
