package kr.hhplus.be.server.domain.coupon;



import lombok.Getter;

@Getter
public class CouponCommand {

    long userId;

    long couponId;

    public CouponCommand(long userId, long couponId) {
        this.userId = userId;
        this.couponId = couponId;
    }

}







