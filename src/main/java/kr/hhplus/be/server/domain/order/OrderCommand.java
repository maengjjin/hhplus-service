package kr.hhplus.be.server.domain.order;

import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.order.OrderCriteria.OrderItem;
import lombok.Getter;

@Getter
public class OrderCommand {

    long productId;

    long optionId;

    long qty;

    public OrderCommand(long productId, long optionId, long qty) {
        this.productId = productId;
        this.optionId = optionId;
        this.qty = qty;
    }


    public static List<OrderCommand> toCommand(List<OrderCriteria.OrderItem> orderItems) {
        return orderItems.stream()
            .map(order -> new OrderCommand(order.getProductId(), order.getOptionId(), order.getQty()))
            .collect(Collectors.toList());
    }


    @Getter
    public static class OrderItemDetail {

        private long productId;

        private long optionId;

        private long qty;

        private long price;


        public OrderItemDetail(long productId, long optionId, long qty, long price) {
            this.productId = productId;
            this.optionId = optionId;
            this.qty = qty;
            this.price = price;
        }


    }






}
