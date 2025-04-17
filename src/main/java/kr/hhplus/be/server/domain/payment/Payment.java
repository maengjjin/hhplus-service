package kr.hhplus.be.server.domain.payment;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.Exception.CouponException.CouponMinimumAmountNotMetException;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.UserCouponInfo;
import kr.hhplus.be.server.domain.order.OrderCommand;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class Payment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long payId;

    private long orderId;

    private long couponId;

    private long productPrice;

    private long userId;

    private long paidPay;

    private long couponPrice;

    private LocalDateTime createAt;


    // 총 상품 금액 계산
    void totalProductPrice(List<OrderCommand.OrderItemDetail> item){

        this.productPrice = item.stream()
            .mapToLong(i -> i.getQty() * i.getPrice())
            .sum();
    }

    // 쿠폰 사용금액 검증
    void validateCouponCondition(UserCouponInfo userCoupon){

        if(this.productPrice < userCoupon.getMinPurchaseAmount()){
            throw new CouponMinimumAmountNotMetException(userCoupon.getMinPurchaseAmount(), this.productPrice);
        }

    }

    // 쿠폰 금액 계산
    void CouponCalculator(UserCouponInfo userCoupon) {

        // 고정금액
        if (CouponType.FIXED == userCoupon.getType()) {
            this.couponPrice = userCoupon.getDiscountAmount();
        } else {
            // 할인 쿠폰
            long discount = this.productPrice * userCoupon.getDiscountRate() / 100;

            this.couponPrice = Math.min(discount, userCoupon.getMaxDiscountAmount());

        }

    }

    // 총 금액 계산
    void totalAmount(){
        this.paidPay = this.productPrice - this.couponPrice;
    }


    public static Payment create(long orderId, long userId, long couponId) {
        Payment payment = new Payment();
        payment.orderId = orderId;
        payment.userId = userId;
        payment.couponId = couponId;
        return payment;
    }

}
