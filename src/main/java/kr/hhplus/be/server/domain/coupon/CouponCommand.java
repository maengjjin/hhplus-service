package kr.hhplus.be.server.domain.coupon;

import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.application.order.OrderCriteria.OrderItem;
import lombok.Getter;

@Getter
public class CouponCommand {

    long userId;

    long couponId;

    public CouponCommand(long userId, long couponId) {
        this.userId = userId;
        this.couponId = couponId;
    }

    public CouponCommand(long couponId) {
        this.couponId = couponId;
    }
}







