package kr.hhplus.be.server.application.order;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.payment.Payment;
import lombok.Getter;

@Getter
public class OrderResponse {

    // 결제완료니깐 주문번호, 결제금액,

    String orderNo;

    List<OrderCommand> item;

    long discountAmount;

    long totalAmount;

    long paymentId;

    LocalDateTime orderCreateAt;


    public OrderResponse(Order order, List<OrderCommand> item, Payment payment) {

        this.orderNo = order.getOrderNo();
        this.item = item;
        this.discountAmount = payment.getCouponPrice();
        this.totalAmount = payment.getPaidPay();
        this.paymentId = payment.getPaymentId();
        this.orderCreateAt = order.getCreateAt();


    }
}
