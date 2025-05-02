package kr.hhplus.be.server.domain.order;

import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.product.ProductDTO.ProductOrderResult;
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



    @Getter
    public static class OrderItem {

        private long productId;

        private long optionId;

        private long qty;

        private long price;


        public OrderItem(long productId, long optionId, long qty, long price) {
            this.productId = productId;
            this.optionId = optionId;
            this.qty = qty;
            this.price = price;
        }


        public static List<OrderItem> toCommand(List<ProductOrderResult> orderItems) {
            return orderItems.stream()
                .map(order -> new OrderItem(order.getProductId(), order.getOptionId(), order.getOrderQty(),order.getPrice()))
                .collect(Collectors.toList());
        }


    }





}
