package kr.hhplus.be.server.domain.payment;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.order.OrderPaymentCalculator.PriceSummary;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "payment")
@Getter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private long paymentId;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "coupon_id")
    private long couponId;

    @Column(name = "product_price")
    private long productPrice;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "paid_pay")
    private long paidPay;

    @Column(name = "coupon_price")
    private long couponPrice;

    @Column(name = "create_at")
    private LocalDateTime createAt;



    public static Payment create(long orderId, long userId, long couponId, PriceSummary priceSummary) {
        Payment payment = new Payment();
        payment.orderId = orderId;
        payment.userId = userId;
        payment.couponId = couponId;
        payment.productPrice = priceSummary.getProductPrice();
        payment.paidPay = priceSummary.getTotalPrice();
        payment.couponPrice = priceSummary.getCouponPrice();
        return payment;
    }


}
