package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDateTime;
import kr.hhplus.be.server.Exception.CouponException.CouponAlreadyUsedException;
import kr.hhplus.be.server.Exception.CouponException.CouponExpiredException;
import lombok.Getter;

@Getter
public class UserCouponInfo {


    private final long userId;

    private final long couponId;

    private final String couponYn;

    private final CouponType type;

    // 할인율
    private final int discountRate;

    // 할인 금액
    private final int discountAmount;

    // 최소 사용 금액
    private final int minPurchaseAmount;

    // 최대 할인 금액
    private final int maxDiscountAmount;

    private final LocalDateTime expiresAt;


    public UserCouponInfo(long userId, long couponId, String couponYn, CouponType type,
        int discountRate, int discountAmount, int minPurchaseAmount, int maxDiscountAmount,
        LocalDateTime expiresAt) {
        this.userId = userId;
        this.couponId = couponId;
        this.couponYn = couponYn;
        this.type = type;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.minPurchaseAmount = minPurchaseAmount;
        this.maxDiscountAmount = maxDiscountAmount;
        this.expiresAt = expiresAt;
    }

    void validateUsable(){
        if("Y".equals(couponYn)){
            throw new CouponAlreadyUsedException();
        }
    }

    void validateNotExpired(){
        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new CouponExpiredException();
        }
    }
}
