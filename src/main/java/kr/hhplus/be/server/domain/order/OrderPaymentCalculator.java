package kr.hhplus.be.server.domain.order;

import java.util.List;
import kr.hhplus.be.server.Exception.CouponException.CouponMinimumAmountNotMetException;
import kr.hhplus.be.server.Exception.PointException.InsufficientPointBalanceException;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.ProductDTO.ProductOrderResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class OrderPaymentCalculator {

    private long productPrice;

    private long couponPrice;

    private long totalPrice;



    // 총 상품 금액 계산
    void totalProductPrice(List<ProductOrderResult>  item){

        this.productPrice = item.stream()
            .mapToLong(i -> i.getOrderQty() * i.getPrice())
            .sum();
    }

    // 쿠폰 사용금액 검증
    void validateCouponCondition(long minPurchaseAmount){

        if(this.productPrice < minPurchaseAmount){
            throw new CouponMinimumAmountNotMetException(minPurchaseAmount, this.productPrice);
        }

    }

    // 쿠폰 금액 계산
    void CouponCalculator(UserCoupon userCoupon) {

        // 고정금액
        if (CouponType.FIXED == userCoupon.getCoupon().getType()) {
            this.couponPrice = userCoupon.getCoupon().getDiscountAmount();
        } else {
            // 할인 쿠폰
            long discount = this.productPrice * userCoupon.getCoupon().getDiscountRate() / 100;

            this.couponPrice = Math.min(discount, userCoupon.getCoupon().getMaxDiscountAmount());

        }

    }

    // 총 금액 계산
    void totalAmount(){
        this.totalPrice = this.productPrice - this.couponPrice;
    }


    void validateSufficientPoint(long point){

        if (totalPrice > point){
            throw new InsufficientPointBalanceException();
        }

    }

    public PriceSummary calculateOrderAmount(long point, UserCoupon userCoupon, List<ProductOrderResult> productOrderResultList) {

        totalProductPrice(productOrderResultList);

        validateCouponCondition(userCoupon.getCoupon().getMinPurchaseAmount());

        CouponCalculator(userCoupon);

        totalAmount();

        validateSufficientPoint(point);

        return new PriceSummary(this.totalPrice, this.productPrice, this.couponPrice);
    }


    @Getter
    @AllArgsConstructor
    public static class PriceSummary {

        private long totalPrice;

        private long productPrice;

        private long couponPrice;


    }

}
