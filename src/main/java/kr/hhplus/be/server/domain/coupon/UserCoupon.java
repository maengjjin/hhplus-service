package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userCouponId;

    private long couponId;

    private long userId;

    private char couponYn;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @Builder
    public UserCoupon(long couponId, long userId, char couponYn) {
        this.couponId = couponId;
        this.userId = userId;
        this.couponYn = couponYn;
    }
}
