package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hhplus.be.server.Exception.CouponException.CouponOutOfStockException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private long couponId;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CouponType type;

    @Column(name = "discount_rate")
    private int discountRate;

    @Column(name = "discount_amount")
    private int discountAmount;

    @Column(name = "min_purchase_amount")
    private int minPurchaseAmount;

    @Column(name = "max_discount_amount")
    private int maxDiscountAmount;

    @Column(name = "issued_qty")
    private int issuedQty;

    @Column(name = "left_qty")
    private int leftQty;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;


    public Coupon(String name, CouponType type, int discountRate, int discountAmount, int minPurchaseAmount, int maxDiscountAmount, int issuedQty, int leftQty, LocalDateTime expiresAt) {
        this.name = name;
        this.type = type;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.minPurchaseAmount = minPurchaseAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.issuedQty = issuedQty;
        this.leftQty = leftQty;
        this.expiresAt = expiresAt;
    }

    public Coupon(long couponId){
        this.couponId = couponId;
    }



    public void validateAndDecreaseLeftQty(){

        if(leftQty <= 0){
            throw new CouponOutOfStockException();
        }
        leftQty--;
    }


}
