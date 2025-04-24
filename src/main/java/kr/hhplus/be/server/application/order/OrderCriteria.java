package kr.hhplus.be.server.application.order;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCriteria {

    private long userId;

    private long couponId;

    private List<OrderItem> items;


    public OrderCriteria(long userId, long couponId, List<OrderItem> items) {
        this.userId = userId;
        this.couponId = couponId;
        this.items = items;
    }


    @Getter
    public static class OrderItem{

        long productId;

        long optionId;

        long qty;

        public OrderItem(long productId, long optionId, long qty) {
            this.productId = productId;
            this.optionId = optionId;
            this.qty = qty;
        }


        public static OrderItem of(long productId, long optionId, long qty){
            return new OrderItem(productId, optionId, qty);
        }


    }




}
