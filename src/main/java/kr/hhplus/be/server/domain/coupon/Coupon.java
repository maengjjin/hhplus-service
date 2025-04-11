package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long couponId;

    private String name;

    private CouponType type;

    private int discountRate;

    private int discountAount;

    private int minPurchaseAmount;

    private int maxDiscountAmount;

    private int issuedQty;

    private int leftQty;

    private LocalDateTime expiresAt;


}
