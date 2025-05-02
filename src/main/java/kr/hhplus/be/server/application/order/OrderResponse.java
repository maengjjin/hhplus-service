package kr.hhplus.be.server.application.order;

import java.util.List;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.product.ProductDTO.ProductOrderResult;
import lombok.Getter;

@Getter
public class OrderResponse {


    String orderNo;

    List<OrderItemResponse> item;

    long discountAmount;

    long totalAmount;

    long paymentId;


    public OrderResponse(String orderNo, OrderItems items, Payment payment) {

        this.orderNo = orderNo;
        this.item = items.toResponse();
        this.discountAmount = payment.getCouponPrice();
        this.totalAmount = payment.getPaidPay();
        this.paymentId = payment.getPaymentId();

    }

    @Getter
    public static class OrderItems {

        private  final List<ProductOrderResult> items;

        private OrderItems(List<ProductOrderResult> items) {
            this.items = List.copyOf(items); // 불변화
        }

        public static OrderItems of(List<ProductOrderResult> items) {
            return new OrderItems(items);
        }


        public  List<OrderItemResponse> toResponse() {
            return items.stream()
                .map(OrderItemResponse::from)
                .toList();
        }


    }


    public static class OrderItemResponse {


        private long orderQty;

        private long price;

        private String productName;

        private String optionName;


        public OrderItemResponse(long orderQty, long price, String productName, String optionName) {
            this.orderQty = orderQty;
            this.price = price;
            this.productName = productName;
            this.optionName = optionName;
        }

        public static OrderItemResponse from(ProductOrderResult orderResult) {
            return new OrderItemResponse(
                orderResult.getProductId(),
                orderResult.getPrice(),
                orderResult.getOptionName(),
                orderResult.getOptionName()
            );
        }
    }

}
