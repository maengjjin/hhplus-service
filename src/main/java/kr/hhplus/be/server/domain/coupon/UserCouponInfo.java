package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDateTime;

public class UserCouponInfo {


    long userId;

    long couponId;

    private CouponType type;

    private int discountRate;

    private int discountAount;

    private int minPurchaseAmount;

    private int maxDiscountAmount;

    private LocalDateTime expiresAt;


    public UserCouponInfo(long userId, long couponId, CouponType type, int discountRate,
        int discountAount, int minPurchaseAmount, int maxDiscountAmount, LocalDateTime expiresAt) {
        this.userId = userId;
        this.couponId = couponId;
        this.type = type;
        this.discountRate = discountRate;
        this.discountAount = discountAount;
        this.minPurchaseAmount = minPurchaseAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.expiresAt = expiresAt;
    }
}
