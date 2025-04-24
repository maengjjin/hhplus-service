package kr.hhplus.be.server.domain.order;

import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.order.OrderCriteria.OrderItem;
import lombok.Getter;


public class OrderCommand {


    @Getter
    public static class OrderItem {

        long productId;

        long optionId;

        long qty;

        public OrderItem(long productId, long optionId, long qty) {
            this.productId = productId;
            this.optionId = optionId;
            this.qty = qty;
        }


        public static List<OrderItem> toCommand(List<OrderCriteria.OrderItem> orderItems) {
            return orderItems.stream()
                .map(order -> new OrderItem(order.getProductId(), order.getOptionId(), order.getQty()))
                .collect(Collectors.toList());
        }


    }



}
