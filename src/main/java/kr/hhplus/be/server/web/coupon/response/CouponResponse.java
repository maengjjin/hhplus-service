package kr.hhplus.be.server.web.coupon.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CouponResponse {

    private long couponId;

    private String name;

    private long discountAmount;

    private String expiresAt;


    public static CouponResponse of(long couponId, String name,long discountAmount, String expiresAt) {
        return new CouponResponse(couponId, name, discountAmount, expiresAt);
    }

}
